package com.solve.global.config.websocket

import com.solve.global.websocket.handler.ProgressWebSocketHandler
import com.solve.global.websocket.handler.RunWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val runWebSocketHandler: RunWebSocketHandler,
    private val progressWebSocketHandler: ProgressWebSocketHandler
) : WebSocketConfigurer {
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry
            .addHandler(runWebSocketHandler, "/ws/run/{runId}")
            .setAllowedOrigins("*")
        registry
            .addHandler(progressWebSocketHandler, "/ws/progress/{submitId}")
            .setAllowedOrigins("*")
    }
}