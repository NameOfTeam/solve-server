package com.solve.global.websocket.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.solve.domain.submit.domain.enums.SubmitState
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

    fun sendProgressUpdate(submitId: Long, progress: Double, state: SubmitState) {
        val session = sessions[submitId] ?: return
        val progressResponse = SubmitProgressResponse(
            submitId = submitId,
            progress = progress,
            result = state
        )

        if (session.isOpen) {
            session.sendMessage(TextMessage(ObjectMapper().writeValueAsString(progressResponse)))
        }
    }
}