package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.ProblemSubmit
import com.solve.domain.problem.domain.enums.ProblemSubmitLanguage
import com.solve.domain.problem.domain.enums.ProblemSubmitResult
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
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@Service
class ProblemSubmitServiceImpl(
    private val securityHolder: SecurityHolder,
    private val submitProperties: SubmitProperties,
    private val problemRepository: ProblemRepository,
    private val problemSubmitRepository: ProblemSubmitRepository,
    private val simpMessageSendingOperations: SimpMessageSendingOperations
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
            result = ProblemSubmitResult.PENDING
        )

        problemSubmitRepository.save(submit)

        processSubmit(submit.id!!, request)

        return ProblemSubmitResponse.of(submit)
    }

    @Async
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
        var progress = 0
        var memoryLimitExceeded = false
        var timeLimitExceeded = false
        val directory = File(submitProperties.path)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory,"${submit.id}.py")

        file.writeText(request.code)

        for (testCase in testCases) {
            if (memoryLimitExceeded) {
                break
            }

            val processBuilder = ProcessBuilder("python3", file.absolutePath)
            val process = processBuilder.start()
            val pid = process.pid()

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

            val outputStream = process.outputStream

            outputStream.write(testCase.input.toByteArray())
            outputStream.flush()
            outputStream.close()

            val timeLimit = problem.timeLimit
            val finishedInTime = process.waitFor(timeLimit, TimeUnit.MILLISECONDS)

            if (!finishedInTime) {
                timeLimitExceeded = true
                process.destroy()
                break
            }

            val output = process.inputStream.bufferedReader().readLines().joinToString("\n")

            if (output != testCase.output) {
                submit.result = ProblemSubmitResult.WRONG_ANSWER
                problemSubmitRepository.save(submit)

                println("Wrong Answer")
                println("Expected: ${testCase.output}")
                println("Received: $output")

                sendProgress(
                    ProblemSubmitProgressResponse(
                        submitId = submit.id!!,
                        result = ProblemSubmitResult.WRONG_ANSWER,
                        progress = progress
                    )
                )

                return
            }

            progress += 100 / testCases.size

            sendProgress(
                ProblemSubmitProgressResponse(
                    submitId = submit.id!!,
                    result = ProblemSubmitResult.PENDING,
                    progress = progress
                )
            )
        }

        if (memoryLimitExceeded) submit.result = ProblemSubmitResult.MEMORY_LIMIT_EXCEEDED
        else if (timeLimitExceeded) submit.result = ProblemSubmitResult.TIME_LIMIT_EXCEEDED
        else submit.result = ProblemSubmitResult.ACCEPTED
        problemSubmitRepository.save(submit)

        sendProgress(
            ProblemSubmitProgressResponse(
                submitId = submit.id!!,
                result = submit.result,
                progress = 100
            )
        )
    }

    private fun processJavaSubmit(submit: ProblemSubmit, request: ProblemSubmitRequest) {
        TODO("Not implemented")
    }

    private fun sendProgress(submit: ProblemSubmitProgressResponse) {
        println(submit)

        simpMessageSendingOperations.convertAndSend("/sub/progress/${submit.submitId}", submit.result)
    }

    private fun getMemoryUsage(pid: Long): Long {
        try {
            val pb = ProcessBuilder("sh", "-c", "ps -o rss= -p $pid")
            val process = pb.start()

            process.inputStream.bufferedReader().useLines { lines ->
                for (line in lines) {
                    if (line.isNotBlank()) {
                        return line.trim().toLong()
                    }
                }
            }

            return -1
        } catch (e: Exception) {
            return -1
        }
    }
}