package com.solve.global.security.jwt.dto

data class JwtResponse(
    val accessToken: String,
    val refreshToken: String
)
