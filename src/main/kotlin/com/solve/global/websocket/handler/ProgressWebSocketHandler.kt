package com.solve.global.websocket.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.solve.domain.submit.domain.enums.SubmitState
import com.solve.domain.submit.dto.request.UpdateProgressRequest
import com.solve.domain.submit.dto.response.SubmitProgressResponse
import com.solve.domain.submit.repository.SubmitQueueRepository
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import java.util.concurrent.ConcurrentHashMap

@Component
class ProgressWebSocketHandler(
    private val submitQueueRepository: SubmitQueueRepository
) : WebSocketHandler {
    private val sessions = ConcurrentHashMap<Long, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val submitId = session.uri?.path?.substringAfterLast("/")?.toLongOrNull()
        if (submitId != null) {
            sessions[submitId] = session
            submitQueueRepository.push(submitId)
        }
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {}

    override fun handleTransportError(session: WebSocketSession, exception: Throwable) {
        session.close()
    }

    override fun afterConnectionClosed(session: WebSocketSession, closeStatus: CloseStatus) {
        val submitId = session.uri?.path?.substringAfterLast("/")?.toLongOrNull()
        if (submitId != null) {
            sessions.remove(submitId)
        }
    }

    override fun supportsPartialMessages(): Boolean = false

    fun sendProgressUpdate(request: UpdateProgressRequest) {
        val session = sessions[request.submitId] ?: return

        if (request.state == SubmitState.ACCEPTED && session.isOpen) {
            val progressResponse = SubmitProgressResponse(
                submitId = request.submitId,
                progress = request.progress,
                result = request.state,
                language = request.language,
                timeUsage = request.timeUsage,
                memoryUsage = request.memoryUsage,
            )

            session.sendMessage(TextMessage(ObjectMapper().writeValueAsString(progressResponse)))
            session.close()

        } else if (request.state ==
            SubmitState.JUDGING ||
            request.state == SubmitState.PENDING ||
            request.state == SubmitState.JUDGING_IN_PROGRESS &&
            session.isOpen) {
            val progressResponse = SubmitProgressResponse(
                submitId = request.submitId,
                progress = request.progress,
                result = request.state,
            )

            session.sendMessage(TextMessage(ObjectMapper().writeValueAsString(progressResponse)))
        } else {
            val progressResponse = SubmitProgressResponse(
                submitId = request.submitId,
                progress = request.progress,
                result = request.state,
                language = request.language
            )

            session.sendMessage(TextMessage(ObjectMapper().writeValueAsString(progressResponse)))
            session.close()
        }
    }
}