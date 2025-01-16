package com.solve.global.config.websocket

import com.solve.domain.run.service.RunService
import com.solve.domain.run.util.handler.RunWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val runService: RunService
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(RunWebSocketHandler(runService), "/ws/run/{runId}")
            .setAllowedOrigins("*")
    }
}