package com.solve.domain.admin.auth.dto.request

data class AdminSignUpRequest(
    val email: String,
    val password: String,
    val username: String,
    val key: String
)