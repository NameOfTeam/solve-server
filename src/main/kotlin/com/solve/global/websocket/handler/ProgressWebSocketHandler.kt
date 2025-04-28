package com.solve.global.websocket.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.solve.domain.submit.dto.request.UpdateProgressRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap

@Component
class ProgressWebSocketHandler(
    private val objectMapper: ObjectMapper
) : TextWebSocketHandler() {

    private val log = LoggerFactory.getLogger(javaClass)
    private val submitSessions = ConcurrentHashMap<String, MutableSet<WebSocketSession>>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val submitId = getSubmitIdFromSession(session)
        if (submitId != null) {
            submitSessions.computeIfAbsent(submitId) { ConcurrentHashMap.newKeySet() }.add(session)
            log.info("WebSocket 연결 생성: {} (제출 ID: {})", session.id, submitId)
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val submitId = getSubmitIdFromSession(session)
        if (submitId != null) {
            val sessions = submitSessions[submitId]
            sessions?.remove(session)
            if (sessions.isNullOrEmpty()) {
                submitSessions.remove(submitId)
            }
            log.info("WebSocket 연결 종료: {} (제출 ID: {})", session.id, submitId)
        }
    }

    private fun getSubmitIdFromSession(session: WebSocketSession): String? {
        val path = session.uri?.path ?: return null
        val segments = path.split("/")
        return if (segments.isNotEmpty()) segments.last() else null
    }

    fun sendProgressUpdate(progressData: UpdateProgressRequest) {
        val submitId = progressData.submitId.toString()
        val sessions = submitSessions[submitId] ?: return

        try {
            val jsonMessage = objectMapper.writeValueAsString(progressData)
            val message = TextMessage(jsonMessage)

            for (session in sessions) {
                if (session.isOpen) {
                    session.sendMessage(message)
                }
            }
        } catch (e: Exception) {
            log.error("진행 상황 메시지 전송 실패 (제출 ID: {}): {}", submitId, e.message)
        }
    }
}