package com.solve.domain.problem.util.handler


import com.fasterxml.jackson.databind.ObjectMapper
import com.solve.domain.problem.service.impl.ProblemRunService
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap

class CodeExecutionWebSocketHandler(
    private val problemRunService: ProblemRunService,
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
            problemRunService.startExecution(runId, session)
        } catch (e: Exception) {
            session.close(CloseStatus.BAD_DATA)
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val runId = session.attributes["runId"] as? String
        sessions.remove(session.id)
        if (runId != null) {
            problemRunService.stopCode(runId)
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val runId = session.attributes["runId"] as? String
        if (runId == null) {
            session.sendMessage(TextMessage("""{"type":"error","content":"Run ID not found"}"""))
            return
        }

        val messageData = try {
            ObjectMapper().readValue(message.payload, WebSocketMessage::class.java)
        } catch (e: Exception) {
            session.sendMessage(TextMessage("""{"type":"error","content":"Invalid message format"}"""))
            return
        }

        when (messageData.type) {
            "input" -> messageData.input?.let { input ->
                problemRunService.handleInput(runId, input)
            }
            "stop" -> {
                problemRunService.stopCode(runId)
                session.close()
            }
            else -> session.sendMessage(TextMessage("""{"type":"error","content":"Unknown message type"}"""))
        }
    }
}

data class WebSocketMessage(
    val type: String = "",
    val input: String? = null
)