package com.solve.domain.submit.listener

import com.fasterxml.jackson.databind.ObjectMapper
import com.solve.domain.submit.domain.enums.SubmitState
import com.solve.domain.submit.dto.request.UpdateProgressRequest
import com.solve.global.common.enums.ProgrammingLanguage
import com.solve.global.websocket.handler.ProgressWebSocketHandler
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.stereotype.Component

@Component
class ProgressListener(
    private val objectMapper: ObjectMapper,
    private val progressWebSocketHandler: ProgressWebSocketHandler
) : MessageListener {

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            val json = String(message.body)
            val node = objectMapper.readTree(json)

            val submissionId = node.get("submissionId").asText()
            val status = node.get("status").asText()
            val progress = node.get("progress")?.asDouble() ?: 0.0

            val state = when {
                status == "COMPILING" -> SubmitState.COMPILING
                status == "RUNNING" && node.has("verdict") -> {
                    when (node.get("verdict").asText()) {
                        "ACCEPTED" -> SubmitState.ACCEPTED
                        "WRONG_ANSWER" -> SubmitState.WRONG_ANSWER
                        "RUNTIME_ERROR" -> SubmitState.RUNTIME_ERROR
                        "TIME_LIMIT_EXCEEDED" -> SubmitState.TIME_LIMIT_EXCEEDED
                        "MEMORY_LIMIT_EXCEEDED" -> SubmitState.MEMORY_LIMIT_EXCEEDED
                        "PRESENTATION_ERROR" -> SubmitState.PRESENTATION_ERROR
                        else -> SubmitState.JUDGING_IN_PROGRESS
                    }
                }
                status == "RUNNING" -> SubmitState.JUDGING_IN_PROGRESS
                else -> SubmitState.JUDGING
            }

            val timeUsage = node.get("timeUsed")?.asLong()
            val memoryUsage = node.get("memoryUsed")?.asLong()
            val language = node.get("language")?.asText()?.let {
                try {
                    ProgrammingLanguage.valueOf(it)
                } catch (e: Exception) {
                    null
                }
            }

            val updateRequest = UpdateProgressRequest(
                submitId = submissionId.toLong(),
                progress = progress,
                state = state,
                timeUsage = timeUsage,
                memoryUsage = memoryUsage,
                language = language
            )

            progressWebSocketHandler.sendProgressUpdate(updateRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}