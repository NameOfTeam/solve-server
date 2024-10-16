package com.solve.domain.user.dto.response

import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.domain.enums.UserRole
import java.util.*

data class UserResponse(
    val id: UUID,
    val username: String,
    val email: String,
    val role: UserRole,
) {
    companion object {
        fun of(user: User) = UserResponse(
            id = user.id!!,
            username = user.username,
            email = user.email,
            role = user.role,
        )
    }
}