package com.solve.domain.user.dto.response

import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.domain.enums.UserRole
import java.util.*

data class UserMeResponse(
    val id: UUID,
    val username: String,
    val email: String,
    val role: UserRole
) {
    companion object {
        fun of(me: User) = UserMeResponse(
            id = me.id!!,
            username = me.username,
            email = me.email,
            role = me.role
        )
    }
}