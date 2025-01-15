package com.solve.global.config.websocket

import com.solve.domain.problem.service.ProblemRunService
import com.solve.domain.problem.util.handler.CodeExecutionWebSocketHandler
import com.solve.global.config.websocket.interceptor.WebSocketInterceptor
import com.solve.global.security.jwt.provider.JwtProvider
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

    @Autowired
    private lateinit var jwtProvider: JwtProvider

    @Autowired
    private lateinit var socketInterceptor: WebSocketInterceptor

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(CodeExecutionWebSocketHandler(problemRunService, jwtProvider), "/ws/run")
            .setAllowedOrigins("*").addInterceptors(socketInterceptor)
    }
}