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
import com.solve.domain.problem.repository.ProblemSubmitQueueRepository
import com.solve.domain.problem.repository.ProblemSubmitRepository
import com.solve.domain.problem.service.ProblemSubmitService
import com.solve.domain.user.repository.UserRepository
import com.solve.global.config.file.FileProperties
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit


@Service
class ProblemSubmitServiceImpl(
    private val securityHolder: SecurityHolder,
    private val fileProperties: FileProperties,
    private val problemRepository: ProblemRepository,
    private val problemSubmitRepository: ProblemSubmitRepository,
    private val problemSubmitQueueRepository: ProblemSubmitQueueRepository,
    private val simpMessageSendingOperations: SimpMessageSendingOperations,
    private val userRepository: UserRepository,
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
        problemSubmitQueueRepository.push(submit.id!!)

        return ProblemSubmitResponse.of(submit)
    }

    @Scheduled(fixedRate = 1000)
    fun processQueue() {
        if (problemSubmitQueueRepository.size() == 0) return

        val submitId = problemSubmitQueueRepository.pop() ?: return
        val submit = problemSubmitRepository.findByIdOrNull(submitId)
            ?: throw CustomException(ProblemSubmitError.PROBLEM_SUBMIT_NOT_FOUND)

        println("Processing ${submit.id}")

        val request = ProblemSubmitRequest(
            code = submit.code,
            language = submit.language,
            visibility = submit.visibility
        )

        CompletableFuture.runAsync {
            processSubmit(submit, request)
        }
    }

    fun processSubmit(submit: ProblemSubmit, request: ProblemSubmitRequest) {
        when (request.language) {
            ProblemSubmitLanguage.PYTHON -> processPythonSubmit(submit, request)
            ProblemSubmitLanguage.JAVA -> processJavaSubmit(submit, request)
            else -> throw CustomException(ProblemError.LANGUAGE_NOT_SUPPORTED)
        }
    }

    private fun processPythonSubmit(submit: ProblemSubmit, request: ProblemSubmitRequest) {
        val problem = submit.problem
        val testCases = problem.testCases.shuffled()
        var progress = 0.0
        var memoryLimitExceeded = false
        var timeLimitExceeded = false
        val directory = File(fileProperties.path, "submits")
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

            val processBuilder = ProcessBuilder("python3", "-W", "ignore", file.absolutePath)
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

            Thread {
                val stream = process.errorStream.bufferedReader()

                while (true) {
                    val line = try {
                        stream.readLine() ?: break
                    } catch (e: IOException) {
                        e.printStackTrace()

                        break
                    }

                    errorSb.append(line).append("\n")
                }
            }.start()

            Thread {
                while (process.isAlive) {
                    val memory = getMemoryUsage(pid)

                    if (memory > (submit.memoryUsage ?: 0)) {
                        submit.memoryUsage = memory
                    }

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
            val finishedInTime = process.waitFor((timeLimit * 1000).toLong(), TimeUnit.MILLISECONDS)

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

        val author = submit.author

        // if the user has no accepted submissions
        if (problemSubmitRepository.findAllByAuthor(author).none { it.state == ProblemSubmitState.ACCEPTED }) {
            val today = LocalDate.now()

            if (author.lastAcceptedAt == null) {
                author.streak = 1
            } else if (author.lastAcceptedAt == today) {
                // 오늘 이미 제출함, 변화 없음
            } else if (author.lastAcceptedAt == today.minusDays(1)) {
                author.streak++
            } else {
                author.streak = 1
            }

            author.lastAcceptedAt = today
            userRepository.save(author)
        }
    }

    private fun processJavaSubmit(submit: ProblemSubmit, request: ProblemSubmitRequest) {
        TODO()
    }

    private fun sendProgress(submit: ProblemSubmitProgressResponse) {
        simpMessageSendingOperations.convertAndSend("/sub/progress/${submit.submitId}", submit)
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