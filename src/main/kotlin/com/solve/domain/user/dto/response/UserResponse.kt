package com.solve.domain.user.dto.response

import com.solve.domain.user.domain.enums.UserRole

data class UserResponse(
    val username: String,
    val email: String,
    val role: UserRole
)