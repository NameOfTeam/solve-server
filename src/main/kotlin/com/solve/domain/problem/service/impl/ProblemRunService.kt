package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.ProblemRun
import com.solve.domain.problem.dto.request.RunCodeRequest
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.repository.ProblemRunRepository
import com.solve.domain.problem.util.CodeRunner
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.config.file.FileProperties
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Service
class ProblemRunService(
    private val problemRepository: ProblemRepository,
    private val problemRunRepository: ProblemRunRepository,
    private val fileProperties: FileProperties,
    private val userRepository: UserRepository,
    private val securityHolder: SecurityHolder,
) {
    private val runningProcesses = ConcurrentHashMap<String, CodeRunner>()

    // HTTP 요청으로 초기 실행 준비
    fun initializeRun(request: RunCodeRequest): String {
        val author = userRepository.findByEmail(securityHolder.user.email)
            ?: throw CustomException(UserError.USER_NOT_FOUND_BY_EMAIL)

        val run = ProblemRun(
            author = author,
            code = request.code,
            language = request.language
        )

        val savedRun = problemRunRepository.save(run)
        return savedRun.id.toString()
    }

    fun startExecution(runId: String, session: WebSocketSession) {
        val run = problemRunRepository.findByIdOrNull(runId.toLong())
            ?: throw CustomException(ProblemError.PROBLEM_NOT_AUTHORIZED)

        val codeRunner = CodeRunner(run.language, fileProperties)
        runningProcesses[runId] = codeRunner

        session.sendMessage(TextMessage("""{"type":"status","content":"Started code execution"}"""))

        val process = codeRunner.execute(runId, run.code)

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
                session.close()
            } catch (e: Exception) {
                session.sendMessage(TextMessage("""{"type":"error","content":"Execution failed: ${e.message}"}"""))
                session.close()
            } finally {
                runningProcesses.remove(runId)
            }
        }.start()
    }

    fun handleInput(runId: String, input: String?) {
        input?.let {
            runningProcesses[runId]?.sendInput(it)
        }
    }

    fun stopCode(runId: String) {
        runningProcesses[runId]?.stop()
        runningProcesses.remove(runId)
    }

    private fun sendOutput(session: WebSocketSession, output: String) {
        if (session.isOpen) {
            session.sendMessage(TextMessage("""{"type":"output","content":"$output"}"""))
        }
    }
}