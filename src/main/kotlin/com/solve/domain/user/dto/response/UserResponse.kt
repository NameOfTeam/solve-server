package com.solve.domain.user.dto.response

import com.solve.domain.user.domain.enums.UserRole

data class GetUserResponse(
    val email: String,
    val role: UserRole
)