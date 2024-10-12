package com.solve.domain.user.dto.request

data class UserMeUpdateRequest(
    val username: String?,
    val email: String?,

    val password: String?,
    val currentPassword: String?
)