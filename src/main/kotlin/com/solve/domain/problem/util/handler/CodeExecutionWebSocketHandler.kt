package com.solve.domain.problem.util.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.solve.domain.problem.service.ProblemRunService
import com.solve.domain.problem.service.RunCodeRequest
import com.solve.global.common.enums.ProgrammingLanguage
import com.solve.global.security.jwt.provider.JwtProvider
import org.springframework.web.socket.*
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.ConcurrentHashMap

class CodeExecutionWebSocketHandler(
    private val problemRunService: ProblemRunService,
    private val jwtProvider: JwtProvider,
) : TextWebSocketHandler() {
    private val sessions = ConcurrentHashMap<String, WebSocketSession>()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val headers = session.handshakeHeaders
        val authToken = headers["Authorization"]?.firstOrNull()?.removePrefix("Bearer ")

        if (authToken.isNullOrBlank()) {
            session.close(CloseStatus.BAD_DATA)
            return
        }

        try {
            val userEmail = jwtProvider.getEmail(authToken)
            sessions[session.id] = session
            session.attributes["user"] = userEmail
        } catch (e: Exception) {
            session.close(CloseStatus.BAD_DATA)
        }

    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.remove(session.id)
        problemRunService.stopCode(session.id)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("Received message: ${message.payload}")

        val messageData = try {
            ObjectMapper().readValue(message.payload, WebSocketMessage::class.java)
        } catch (e: Exception) {
            session.sendMessage(TextMessage("""{"type":"error","content":"Invalid message format"}"""))
            return
        }

        when (messageData.type) {
            "run" -> handleRunCode(session, messageData)
            "input" -> handleInput(session, messageData)
            "stop" -> handleStop(session)
            else -> session.sendMessage(TextMessage("""{"type":"error","content":"Unknown message type"}"""))
        }
    }

    private fun handleRunCode(session: WebSocketSession, message: WebSocketMessage) {
        val runRequest = RunCodeRequest(
            problemId = message.problemId ?: return,
            code = message.code ?: return,
            language = message.language ?: return
        )
        problemRunService.runCode(runRequest, session)
    }

    private fun handleInput(session: WebSocketSession, message: WebSocketMessage) {
        message.input?.let { input ->
            problemRunService.handleInput(session.id, input)
        }
    }

    private fun handleStop(session: WebSocketSession) {
        problemRunService.stopCode(session.id)
    }
}

data class WebSocketMessage(
    val type: String = "",
    val problemId: Long? = null,
    val code: String? = null,
    val language: ProgrammingLanguage? = null,
    val input: String? = null
)