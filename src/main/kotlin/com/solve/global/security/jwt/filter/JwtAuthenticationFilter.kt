package com.solve.global.security.jwt.filter

import com.solve.global.security.jwt.provider.JwtProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(private val jwtProvider: JwtProvider) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = jwtProvider.extractToken(request)

        if (token != null) {
            SecurityContextHolder.getContext().authentication = jwtProvider.getAuthentication(token)

            // check user is verified

        }

        filterChain.doFilter(request, response)
    }
}