package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.ProblemRun
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.repository.ProblemRunRepository
import com.solve.domain.problem.service.ProblemRunService
import com.solve.domain.problem.service.RunCodeRequest
import com.solve.domain.problem.util.CodeRunner
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.config.file.FileProperties
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Service
class ProblemRunServiceImpl(
    private val problemRepository: ProblemRepository,
    private val problemRunRepository: ProblemRunRepository,
    private val fileProperties: FileProperties,
    private val userRepository: UserRepository,
) : ProblemRunService {
    private val runningProcesses = ConcurrentHashMap<String, CodeRunner>()

    override fun runCode(request: RunCodeRequest, session: WebSocketSession) {
        val problem = problemRepository.findByIdOrNull(request.problemId)
            ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND, request.problemId)

        val author = userRepository.findByEmail(session.attributes["user"].toString())
            ?: throw CustomException(UserError.USER_NOT_FOUND_BY_EMAIL)

        val run = ProblemRun(
            problem = problem,
            author = author,
            code = request.code,
            language = request.language
        )

        val savedRun = problemRunRepository.save(run)
        processRun(savedRun, session)
    }

    override fun stopCode(sessionId: String) {
        runningProcesses[sessionId]?.stop()
        runningProcesses.remove(sessionId)
    }

    override fun handleInput(sessionId: String, input: String) {
        runningProcesses[sessionId]?.sendInput(input)
    }

    private fun processRun(run: ProblemRun, session: WebSocketSession) {
        val codeRunner = CodeRunner(run.language, fileProperties)
        runningProcesses[session.id] = codeRunner

        // 실행 시작 알림
        session.sendMessage(TextMessage("""{"type":"status","content":"Started code execution"}"""))

        val process = codeRunner.execute(run.id.toString(), run.code)

        // 출력 모니터링 스레드
        Thread {
            try {
                process.inputStream.bufferedReader().use { reader ->
                    var buffer = StringBuilder()
                    while (session.isOpen) {
                        val char = reader.read()
                        if (char == -1) break

                        if (char == '\n'.code) {
                            sendOutput(session, buffer.toString())
                            buffer.clear()
                        } else {
                            buffer.append(char.toChar())
                            if (buffer.length >= 1024) {
                                sendOutput(session, buffer.toString())
                                buffer.clear()
                            }
                        }
                    }
                    if (buffer.isNotEmpty()) {
                        sendOutput(session, buffer.toString())
                    }
                }
                session.sendMessage(TextMessage("""{"type":"status","content":"Execution completed"}"""))
            } catch (e: Exception) {
                session.sendMessage(TextMessage("""{"type":"error","content":"Execution failed: ${e.message}"}"""))
            } finally {
                runningProcesses.remove(session.id)
            }
        }.start()
    }

    private fun sendOutput(session: WebSocketSession, output: String) {
        if (session.isOpen) {
            session.sendMessage(TextMessage("""{"type":"output","content":"$output"}"""))
        }
    }
}