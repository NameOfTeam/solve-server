package com.solve.global.websocket.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.dto.response.ProblemSubmitProgressResponse
import com.solve.domain.problem.repository.ProblemSubmitQueueRepository
import org.springframework.stereotype.Component
import org.springframework.web.socket.*
import java.util.concurrent.ConcurrentHashMap

@Component
class ProgressWebSocketHandler(
    private val problemSubmitQueueRepository: ProblemSubmitQueueRepository
) : WebSocketHandler {
    private val sessions = ConcurrentHashMap<Long, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val submitId = session.uri?.path?.substringAfterLast("/")?.toLongOrNull()
        if (submitId != null) {
            sessions[submitId] = session
            problemSubmitQueueRepository.push(submitId)
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

    fun sendProgressUpdate(submitId: Long, progress: Double, state: ProblemSubmitState) {
        val session = sessions[submitId] ?: return
        val progressResponse = ProblemSubmitProgressResponse(
            submitId = submitId,
            progress = progress,
            result = state
        )

        if (session.isOpen) {
            session.sendMessage(TextMessage(ObjectMapper().writeValueAsString(progressResponse)))
        }
    }
}