package com.solve.global.config.websocket.interceptor

import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.error.UserError
import com.solve.global.error.CustomException
import com.solve.global.security.jwt.error.JwtError
import com.solve.global.security.jwt.provider.JwtProvider
import org.springframework.http.HttpHeaders
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class WebSocketInterceptor(
    private val jwtProvider: JwtProvider
) : HandshakeInterceptor {
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val headers = request.headers
        val token = headers.getFirst(HttpHeaders.AUTHORIZATION) ?: throw CustomException(JwtError.INCORRECT_TOKEN)

        if (token.startsWith("Bearer ")) {
            attributes["Authorization"] = token
            return true
        }

        return false
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        // 필요 시 후처리 로직 작성
    }
}
