package com.solve.domain.admin.user.dto.response

import com.solve.domain.user.domain.entity.User
import java.util.*

data class AdminUserResponse(
    val id: UUID,
    val email: String,
    val username: String,
    val role: String
) {
    companion object {
        fun of(user: User) = AdminUserResponse(
            id = user.id!!,
            email = user.email,
            username = user.username,
            role = user.role.name
        )
    }
}
