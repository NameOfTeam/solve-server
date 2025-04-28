package com.solve.domain.submit.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.solve.domain.submit.domain.enums.SubmitState
import com.solve.domain.submit.dto.request.UpdateProgressRequest
import com.solve.domain.submit.error.SubmitError
import com.solve.domain.submit.repository.SubmitRepository
import com.solve.domain.user.domain.entity.UserSolved
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import com.solve.global.websocket.handler.ProgressWebSocketHandler
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
class ResultListener(
    private val objectMapper: ObjectMapper,
    private val submitRepository: SubmitRepository,
    private val userRepository: UserRepository,
    private val progressWebSocketHandler: ProgressWebSocketHandler
) : MessageListener {

    @Transactional
    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            val json = String(message.body)
            val node = objectMapper.readTree(json)

            val submissionId = node.get("submissionId").asText()
            val resultNode = node.get("result")

            if (resultNode != null && resultNode.has("judgeResult")) {
                val judgeResultStr = resultNode.get("judgeResult").asText()

                val submit = submitRepository.findByIdOrNull(submissionId.toLong())
                    ?: throw CustomException(SubmitError.SUBMIT_NOT_FOUND)

                val state = when (judgeResultStr) {
                    "ACCEPTED" -> SubmitState.ACCEPTED
                    "WRONG_ANSWER" -> SubmitState.WRONG_ANSWER
                    "COMPILATION_ERROR" -> SubmitState.COMPILE_ERROR
                    "RUNTIME_ERROR" -> SubmitState.RUNTIME_ERROR
                    "TIME_LIMIT_EXCEEDED" -> SubmitState.TIME_LIMIT_EXCEEDED
                    "MEMORY_LIMIT_EXCEEDED" -> SubmitState.MEMORY_LIMIT_EXCEEDED
                    "PRESENTATION_ERROR" -> SubmitState.PRESENTATION_ERROR
                    else -> SubmitState.JUDGING
                }

                // 테스트 케이스 결과에서 최대 시간/메모리 사용량 계산
                val testCases = resultNode.get("testCases")
                var maxTimeUsage = 0L
                var maxMemoryUsage = 0L

                if (testCases != null && testCases.isArray()) {
                    for (testCase in testCases) {
                        if (testCase.has("timeUsed")) {
                            val timeUsed = testCase.get("timeUsed").asLong()
                            maxTimeUsage = maxOf(maxTimeUsage, timeUsed)
                        }
                        if (testCase.has("memoryUsed")) {
                            val memoryUsed = testCase.get("memoryUsed").asDouble().toLong()
                            maxMemoryUsage = maxOf(maxMemoryUsage, memoryUsed)
                        }
                    }
                }

                submit.state = state
                submit.timeUsage = maxTimeUsage
                submit.memoryUsage = maxMemoryUsage
                submitRepository.save(submit)

                // WebSocket으로 최종 결과 전송
                val updateRequest = UpdateProgressRequest(
                    submitId = submissionId.toLong(),
                    progress = 100.0,
                    state = state,
                    timeUsage = maxTimeUsage,
                    memoryUsage = maxMemoryUsage,
                    language = submit.language
                )

                progressWebSocketHandler.sendProgressUpdate(updateRequest)

                // ACCEPTED인 경우 사용자 기록 업데이트
                if (state == SubmitState.ACCEPTED) {
                    handleAcceptedSubmission(submit)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleAcceptedSubmission(submit: com.solve.domain.submit.domain.entity.Submit) {
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
            e.printStackTrace()
        }
    }
}