package com.solve.domain.submit.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.repository.ProblemTestCaseRepository
import com.solve.domain.submit.domain.entity.Submit
import com.solve.domain.submit.domain.enums.SubmitState
import com.solve.domain.submit.dto.request.SubmitRequest
import com.solve.domain.submit.dto.request.UpdateProgressRequest
import com.solve.domain.submit.dto.response.SubmitResponse
import com.solve.domain.submit.error.SubmitError
import com.solve.domain.submit.repository.SubmitQueryRepository
import com.solve.domain.submit.repository.SubmitRepository
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.common.enums.ProgrammingLanguage
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import com.solve.global.websocket.handler.ProgressWebSocketHandler
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubmitService(
    private val securityHolder: SecurityHolder,
    private val problemRepository: ProblemRepository,
    private val submitRepository: SubmitRepository,
    private val userRepository: UserRepository,
    private val problemTestCaseRepository: ProblemTestCaseRepository,
    private val progressWebSocketHandler: ProgressWebSocketHandler,
    private val submitQueryRepository: SubmitQueryRepository,
    private val redisTemplate: StringRedisTemplate,
    private val objectMapper: ObjectMapper
) {
    companion object {
        const val REQUEST_CHANNEL = "submission-requests"
        const val PROGRESS_CHANNEL = "submission-progress"
        const val RESULT_CHANNEL = "submission-results"
    }

    @Transactional(readOnly = true)
    fun getMySubmits(problemId: Long, cursorId: Long?, size: Int): List<SubmitResponse> {
        val problem = problemRepository.findByIdOrNull(problemId)
            ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val cursor = cursorId?.let { submitRepository.findByIdOrNull(it) ?: throw CustomException(SubmitError.SUBMIT_NOT_FOUND) }

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

        // 채점 서버에 요청 전송
        sendJudgeRequest(submit)

        return SubmitResponse.of(submit)
    }

    private fun sendJudgeRequest(submit: Submit) {
        try {
            val problem = submit.problem
            val testCases = problemTestCaseRepository.findAllByProblem(problem)

            val judgeRequest = mapOf(
                "submissionId" to submit.id.toString(),
                "code" to submit.code,
                "language" to submit.language.name,
                "problemId" to problem.id.toString(),
                "testCases" to testCases.map {
                    mapOf(
                        "input" to it.input,
                        "output" to it.output
                    )
                },
                "timeLimit" to problem.timeLimit,
                "memoryLimit" to problem.memoryLimit
            )

            val jsonRequest = objectMapper.writeValueAsString(judgeRequest)
            redisTemplate.convertAndSend(REQUEST_CHANNEL, jsonRequest)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("채점 요청 전송 중 오류가 발생했습니다.", e)
        }
    }
}