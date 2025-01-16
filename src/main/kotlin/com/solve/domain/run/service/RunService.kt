package com.solve.domain.run.service

import com.solve.domain.problem.error.ProblemError
import com.solve.domain.run.domain.entity.Run
import com.solve.domain.run.dto.request.RunCodeRequest
import com.solve.domain.run.dto.response.RunResponse
import com.solve.domain.run.repository.RunRepository
import com.solve.domain.run.util.CodeRunner
import com.solve.global.config.file.FileProperties
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Service
class RunService(
    private val runRepository: RunRepository,
    private val fileProperties: FileProperties,
    private val securityHolder: SecurityHolder,
) {
    private val runningProcesses = ConcurrentHashMap<String, CodeRunner>()

    fun runCode(request: RunCodeRequest): RunResponse {
        val author = securityHolder.user

        val run = Run(
            author = author,
            code = request.code,
            language = request.language
        )

        runRepository.save(run)

        return RunResponse(run.id!!)
    }

    fun startRun(runId: String, session: WebSocketSession) {
        val run = runRepository.findByIdOrNull(runId.toLong())
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
            session.sendMessage(TextMessage("""{"type":"output","content":"${output.replace("\"", "\\\"")}"}"""))
        }
    }
}