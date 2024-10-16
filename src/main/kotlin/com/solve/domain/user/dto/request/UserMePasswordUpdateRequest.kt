package com.solve.domain.user.dto.request

data class UserMePasswordUpdateRequest(
    val newPassword: String,
    val currentPassword: String
)