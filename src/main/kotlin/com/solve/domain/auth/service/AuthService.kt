package com.solve.domain.auth.service

import com.solve.domain.auth.dto.request.LoginRequest
import com.solve.domain.auth.dto.request.ReissueRequest
import com.solve.domain.auth.dto.request.SignUpRequest
import com.solve.domain.auth.dto.response.VerifyResponse
import com.solve.global.security.jwt.dto.JwtResponse

interface AuthService {
    fun login(request: LoginRequest): JwtResponse
    fun signup(request: SignUpRequest)
    fun reissue(request: ReissueRequest): JwtResponse
    fun verify(token: String): VerifyResponse
}