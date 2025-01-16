package com.solve.global.config.websocket

import com.solve.domain.problem.service.impl.ProblemRunService
import com.solve.domain.problem.util.handler.CodeExecutionWebSocketHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig : WebSocketConfigurer {
    @Autowired
    private lateinit var problemRunService: ProblemRunService

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(CodeExecutionWebSocketHandler(problemRunService), "/ws/run/{runId}")
            .setAllowedOrigins("*")
    }
}