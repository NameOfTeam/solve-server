package com.solve.global.config.websocket

import com.solve.domain.run.service.RunService
import com.solve.domain.run.util.handler.RunWebSocketHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    @Autowired
    private lateinit var problemRunService: RunService

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(RunWebSocketHandler(problemRunService), "/ws/run/{runId}")
            .setAllowedOrigins("*")
    }
}