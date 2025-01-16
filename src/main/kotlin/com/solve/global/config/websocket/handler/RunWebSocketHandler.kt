package com.solve.global.config.websocket.handler


import com.fasterxml.jackson.databind.ObjectMapper
import com.solve.domain.run.dto.request.RunWebSocketMessage
import com.solve.domain.run.service.RunService
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap

@Component
class RunWebSocketHandler(
    private val runService: RunService,
) : TextWebSocketHandler() {
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val runId = session.uri?.path?.substringAfterLast("/")
        if (runId == null) {
            session.close(CloseStatus.BAD_DATA)
            return
        }

        try {
            sessions[session.id] = session
            session.attributes["runId"] = runId
            runService.startRun(runId, session)
        } catch (e: Exception) {
            session.close(CloseStatus.BAD_DATA)
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val runId = session.attributes["runId"] as? String
        sessions.remove(session.id)
        if (runId != null) {
            runService.stopCode(runId)
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val runId = session.attributes["runId"] as? String
        if (runId == null) {
            session.sendMessage(TextMessage("""{"type":"error","content":"Run ID not found"}"""))
            return
        }

        val messageData = try {
            ObjectMapper().readValue(message.payload, RunWebSocketMessage::class.java)
        } catch (e: Exception) {
            session.sendMessage(TextMessage("""{"type":"error","content":"Invalid message format"}"""))
            return
        }

        when (messageData.type) {
            "input" -> messageData.input?.let { input ->
                runService.handleInput(runId, input)
            }
            "stop" -> {
                runService.stopCode(runId)
                session.close()
            }
            else -> session.sendMessage(TextMessage("""{"type":"error","content":"Unknown message type"}"""))
        }
    }
}