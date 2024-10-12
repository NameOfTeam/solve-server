package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.ProblemSubmit
import com.solve.domain.problem.domain.enums.ProblemSubmitLanguage
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.dto.request.ProblemSubmitRequest
import com.solve.domain.problem.dto.response.ProblemSubmitProgressResponse
import com.solve.domain.problem.dto.response.ProblemSubmitResponse
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.error.ProblemSubmitError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.repository.ProblemSubmitRepository
import com.solve.domain.problem.service.ProblemSubmitService
import com.solve.global.config.SubmitProperties
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit


@Service
class ProblemSubmitServiceImpl(
    private val securityHolder: SecurityHolder,
    private val submitProperties: SubmitProperties,
    private val problemRepository: ProblemRepository,
    private val problemSubmitRepository: ProblemSubmitRepository,
    private val simpMessageSendingOperations: SimpMessageSendingOperations,
) : ProblemSubmitService {
    @Transactional
    override fun submitProblem(problemId: Long, request: ProblemSubmitRequest): ProblemSubmitResponse {
        val author = securityHolder.user
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val submit = ProblemSubmit(
            code = request.code,
            author = author,
            problem = problem,
            language = request.language,
            visibility = request.visibility,
            state = ProblemSubmitState.PENDING
        )

        problemSubmitRepository.save(submit)

        processSubmit(submit.id!!, request)

        return ProblemSubmitResponse.of(submit)
    }

    @Transactional
    fun processSubmit(submitId: Long, request: ProblemSubmitRequest) {
        val submit = problemSubmitRepository.findByIdOrNull(submitId)
            ?: throw CustomException(ProblemSubmitError.PROBLEM_SUBMIT_NOT_FOUND)

        when (request.language) {
            ProblemSubmitLanguage.PYTHON -> processPythonSubmit(submit, request)
            ProblemSubmitLanguage.JAVA -> processJavaSubmit(submit, request)
            else -> throw CustomException(ProblemError.LANGUAGE_NOT_SUPPORTED)
        }
    }

    private fun processPythonSubmit(submit: ProblemSubmit, request: ProblemSubmitRequest) {
        val problem = submit.problem
        val testCases = problem.testCases
        var progress = 0.0
        var memoryLimitExceeded = false
        var timeLimitExceeded = false
        val directory = File(submitProperties.path)
        val size = testCases.size.toDouble()

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "${submit.id}.py")
        file.createNewFile()
        file.writeText(request.code)

        sendProgress(
            ProblemSubmitProgressResponse(
                submitId = submit.id!!,
                progress = progress,
                ProblemSubmitState.JUDGING
            )
        )

        for (testCase in testCases) {
            if (memoryLimitExceeded) {
                break
            }

            val processBuilder = ProcessBuilder("python3", file.absolutePath)
            val process = processBuilder.start()
            val pid = process.pid()

            val sb = StringBuilder()
            val errorSb = StringBuilder()

            Thread {
                val stream = process.inputStream.bufferedReader()

                while (true) {
                    val line = try {
                        stream.readLine() ?: break
                    } catch (e: IOException) {
                        e.printStackTrace()

                        break
                    }

                    sb.append(line).append("\n")
                }
            }.start()

//            Thread {
//                val stream = process.errorStream.bufferedReader()
//
//                while (true) {
//                    val line = try {
//                        stream.readLine() ?: break
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//
//                        break
//                    }
//
//                    errorSb.append(line).append("\n")
//                }
//            }.start()

            Thread {
                while (process.isAlive) {
                    val memory = getMemoryUsage(pid)

                    if (memory > problem.memoryLimit) {
                        memoryLimitExceeded = true

                        process.destroy()

                        return@Thread
                    }
                }
            }.start()

            Thread {
                process.outputStream.use {
                    it.write(testCase.input.toByteArray())
                    it.flush()
                }
            }.start()

            val timeLimit = problem.timeLimit
            val finishedInTime = process.waitFor(timeLimit, TimeUnit.MILLISECONDS)

            if (memoryLimitExceeded) {
                break
            }

            if (!finishedInTime) {
                timeLimitExceeded = true
                process.destroy()
                break
            }

            val output = sb.toString().trim()
            val error = errorSb.toString().trim()

            if (error.isNotEmpty()) {
                submit.state = ProblemSubmitState.RUNTIME_ERROR
                problemSubmitRepository.save(submit)

                println("Runtime Error")
                println("Input:\n${testCase.input}")
                println("Error:\n$error")

                sendProgress(
                    ProblemSubmitProgressResponse(
                        submitId = submit.id,
                        result = ProblemSubmitState.RUNTIME_ERROR,
                        progress = progress,
                    )
                )

                return
            }

            if (output != testCase.output) {
                submit.state = ProblemSubmitState.WRONG_ANSWER
                problemSubmitRepository.save(submit)

                println("Wrong Answer")
                println("Input:\n${testCase.input}")
                println("Expected:\n${testCase.output}")
                println("Received:\n$output")

                sendProgress(
                    ProblemSubmitProgressResponse(
                        submitId = submit.id,
                        result = ProblemSubmitState.WRONG_ANSWER,
                        progress = progress
                    )
                )

                return
            }

            progress += 100.0 / size

            sendProgress(
                ProblemSubmitProgressResponse(
                    submitId = submit.id,
                    result = ProblemSubmitState.JUDGING_IN_PROGRESS,
                    progress = progress
                )
            )
        }

        if (memoryLimitExceeded) submit.state = ProblemSubmitState.MEMORY_LIMIT_EXCEEDED
        else if (timeLimitExceeded) submit.state = ProblemSubmitState.TIME_LIMIT_EXCEEDED
        else submit.state = ProblemSubmitState.ACCEPTED
        problemSubmitRepository.save(submit)

        sendProgress(
            ProblemSubmitProgressResponse(
                submitId = submit.id,
                result = submit.state,
                progress = 100.0
            )
        )
    }

    private fun processJavaSubmit(submit: ProblemSubmit, request: ProblemSubmitRequest) {
        val problem = submit.problem
    }

    private fun sendProgress(submit: ProblemSubmitProgressResponse) {
        simpMessageSendingOperations.convertAndSend("/sub/progress", submit)
    }

    private fun getMemoryUsage(pid: Long): Long {
        try {
            val pb = ProcessBuilder("sh", "-c", "ps -o rss= -p $pid")
            val process = pb.start()

            process.inputStream.bufferedReader().useLines { lines ->
                for (line in lines) {
                    if (line.isNotBlank()) {
                        return line.trim().toLong() / 1024
                    }
                }
            }

            return -1
        } catch (e: Exception) {
            return -1
        }
    }
}