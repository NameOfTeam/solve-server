package com.devox.domain.auth.service

import com.devox.domain.auth.dto.request.LoginRequest
import com.devox.domain.auth.dto.request.ReissueRequest
import com.devox.domain.auth.dto.request.SignUpRequest
import com.devox.global.security.jwt.dto.JwtResponse

interface AuthService {
    fun login(request: LoginRequest): JwtResponse
    fun signup(request: SignUpRequest)
    fun reissue(request: ReissueRequest): JwtResponse
}