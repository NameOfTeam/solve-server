package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.ProblemSubmit
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
import com.solve.domain.problem.util.CodeExecutor
import com.solve.domain.problem.util.DockerCodeExecutor
import com.solve.domain.user.domain.entity.UserSolved
import com.solve.domain.user.repository.UserRepository
import com.solve.global.config.file.FileProperties
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.concurrent.CompletableFuture


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
    @Transactional
    fun processQueue() {
        if (problemSubmitQueueRepository.size() == 0) return

        val submitId = problemSubmitQueueRepository.pop() ?: return
        val submit = problemSubmitRepository.findByIdOrNull(submitId)
            ?: throw CustomException(ProblemSubmitError.PROBLEM_SUBMIT_NOT_FOUND)

        val request = ProblemSubmitRequest(
            code = submit.code,
            language = submit.language,
            visibility = submit.visibility
        )

        CompletableFuture.runAsync {
            processSubmit(submit, request)
        }
    }

    @Transactional
    fun processSubmit(submit: ProblemSubmit, request: ProblemSubmitRequest) {
        val executor = DockerCodeExecutor(submit, request, fileProperties)
        val problem = submit.problem
        val testCases = problem.testCases.shuffled()
        var progress = 0.0
        val totalTestCases = testCases.size.toDouble()
        var maxTimeUsage = 0L  // Long으로 변경

        submit.state = ProblemSubmitState.JUDGING
        updateProgress(submit.id!!, progress, ProblemSubmitState.JUDGING)

        executor.initializeContainer()
        for (testCase in testCases) {
            val result = executor.execute(testCase.input, problem.timeLimit, testCase.output)
            maxTimeUsage = maxOf(maxTimeUsage, result.timeUsage)

            if (!result.success) {
                submit.state = result.state!!
                submit.timeUsage = maxTimeUsage  // timeUsage 필드 사용
                if (result.state == ProblemSubmitState.COMPILE_ERROR) {
//                    submit.compileError = result.compilationOutput
                }
                problemSubmitRepository.save(submit)
                updateProgress(submit.id, progress, result.state)
                return
            }

            progress += 100.0 / totalTestCases
            updateProgress(submit.id, progress, ProblemSubmitState.JUDGING_IN_PROGRESS)
        }

        submit.state = ProblemSubmitState.ACCEPTED
        submit.timeUsage = maxTimeUsage  // timeUsage 필드 사용
        problemSubmitRepository.save(submit)
        updateProgress(submit.id, 100.0, ProblemSubmitState.ACCEPTED)

        handleAcceptedSubmission(submit)
    }

    private fun updateProgress(submitId: Long, progress: Double, state: ProblemSubmitState) {
        val progressResponse = ProblemSubmitProgressResponse(
            submitId = submitId,
            progress = progress,
            result = state
        )
        simpMessageSendingOperations.convertAndSend("/sub/progress/$submitId", progressResponse)
    }

    private fun handleAcceptedSubmission(submit: ProblemSubmit) {
        try {
            val user = submit.author
            val problemNotSolvedYet = user.solved.none { it.problem == submit.problem }

            if (problemNotSolvedYet) {
                user.solved.add(
                    UserSolved(
                        user = user,
                        problem = submit.problem,
                        date = LocalDate.now()
                    )
                )
                userRepository.save(user)
            }
        } catch (e: Exception) {
            // 로깅 추가
            e.printStackTrace()
        }
    }
}