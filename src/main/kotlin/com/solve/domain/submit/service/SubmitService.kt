package com.solve.domain.submit.service

import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.repository.ProblemTestCaseRepository
import com.solve.domain.problem.util.CodeExecutor
import com.solve.domain.submit.domain.entity.Submit
import com.solve.domain.submit.domain.enums.SubmitState
import com.solve.domain.submit.dto.request.SubmitRequest
import com.solve.domain.submit.dto.response.SubmitResponse
import com.solve.domain.submit.error.SubmitError
import com.solve.domain.submit.repository.SubmitQueryRepository
import com.solve.domain.submit.repository.SubmitQueueRepository
import com.solve.domain.submit.repository.SubmitRepository
import com.solve.domain.user.domain.entity.UserSolved
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.common.enums.ProgrammingLanguage
import com.solve.global.config.file.FileProperties
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import com.solve.global.websocket.handler.ProgressWebSocketHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.concurrent.CompletableFuture

@Service
class SubmitService(
    private val securityHolder: SecurityHolder,
    private val fileProperties: FileProperties,
    private val problemRepository: ProblemRepository,
    private val submitRepository: SubmitRepository,
    private val userRepository: UserRepository,
    private val problemTestCaseRepository: ProblemTestCaseRepository,
    private val progressWebSocketHandler: ProgressWebSocketHandler,
    private val submitQueueRepository: SubmitQueueRepository,
    private val submitQueryRepository: SubmitQueryRepository
) {
    @Transactional(readOnly = true)
    fun getMySubmits(problemId: Long, cursorId: Long, size: Int): List<SubmitResponse> {
        val problem = problemRepository.findByIdOrNull(problemId)
            ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val cursor = submitRepository.findByIdOrNull(cursorId)
            ?: throw CustomException(SubmitError.SUBMIT_NOT_FOUND)

        val submits = submitQueryRepository.getMySubmits(problem, cursor, size)

        return submits.map { SubmitResponse.of(it) }
    }

    @Transactional(readOnly = true)
    fun searchSubmit(
        problemId: Long?,
        username: String?,
        state: SubmitState?,
        language: ProgrammingLanguage?,
        cursorId: Long?,
        size: Int
    ): List<SubmitResponse> {
        val problem = problemId?.let {
            problemRepository.findByIdOrNull(it) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        }
        val user = username?.let {
            userRepository.findByUsername(it) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_USERNAME, it)
        }
        val cursor =
            cursorId?.let { submitRepository.findByIdOrNull(it) ?: throw CustomException(SubmitError.SUBMIT_NOT_FOUND) }
        val submits = submitQueryRepository.searchSubmit(problem, user, state, language, cursor, size)

        return submits.map { SubmitResponse.of(it) }
    }

    @Transactional
    fun submitProblem(problemId: Long, request: SubmitRequest): SubmitResponse {
        val author = securityHolder.user
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val submit = Submit(
            code = request.code,
            author = author,
            problem = problem,
            language = request.language,
            visibility = request.visibility,
            state = SubmitState.PENDING
        )

        submitRepository.save(submit)
//        problemSubmitQueueRepository.push(submit.id!!)

        return SubmitResponse.of(submit)
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    fun processQueue() {
        if (submitQueueRepository.size() == 0) return

        val submitId = submitQueueRepository.pop() ?: return
        val submit = submitRepository.findByIdOrNull(submitId)
            ?: throw CustomException(SubmitError.SUBMIT_NOT_FOUND)

        val request = SubmitRequest(
            code = submit.code,
            language = submit.language,
            visibility = submit.visibility
        )

        CompletableFuture.runAsync {
            processSubmit(submit, request)
        }
    }

    //    @Transactional
    fun processSubmit(submit: Submit, request: SubmitRequest) {
        val executor = CodeExecutor(submit, request, fileProperties)
        val problem = submit.problem
        val testCases = problemTestCaseRepository.findAllByProblem(problem)
        var progress = 0.0
        val totalTestCases = testCases.size.toDouble()
        var maxTimeUsage = 0L  // Long으로 변경
        var maxMemoryUsage = 0L

        submit.state = SubmitState.JUDGING
        updateProgress(submit.id!!, progress, SubmitState.JUDGING)

//        executor.initializeJavaContainer()
        for (testCase in testCases) {
            val result = executor.execute(testCase.input, problem.timeLimit, testCase.output)
            maxTimeUsage = maxOf(maxTimeUsage, result.timeUsage)
            maxMemoryUsage = maxOf(maxMemoryUsage, result.memoryUsage)

            if (!result.success) {
                submit.state = result.state!!
                submit.timeUsage = maxTimeUsage  // timeUsage 필드 사용
                submit.memoryUsage = maxMemoryUsage
                if (result.state == SubmitState.COMPILE_ERROR) {
//                    submit.compileError = result.compilationOutput
                }
                submitRepository.save(submit)
                updateProgress(submit.id, progress, result.state)
                return
            }

            progress += 100.0 / totalTestCases
            updateProgress(submit.id, progress, SubmitState.JUDGING_IN_PROGRESS)
        }

        submit.state = SubmitState.ACCEPTED
        submit.timeUsage = maxTimeUsage  // timeUsage 필드 사용
        submit.memoryUsage = maxMemoryUsage
        submitRepository.save(submit)
        updateProgress(submit.id, 100.0, SubmitState.ACCEPTED)

        handleAcceptedSubmission(submit)
    }

    private fun updateProgress(submitId: Long, progress: Double, state: SubmitState) {
        progressWebSocketHandler.sendProgressUpdate(submitId, progress, state)
    }

    private fun handleAcceptedSubmission(submit: Submit) {
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