package com.solve.domain.user.dto.response

import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.domain.enums.UserRole
import java.time.LocalDateTime
import java.util.*

data class UserMeResponse(
    val id: UUID,
    val username: String,
    val email: String,
    val introduction: String? = null,
    val role: UserRole,
    val streak: Int = 0,
    val solvedToday: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun of(me: User) = UserMeResponse(
            id = me.id!!,
            username = me.username,
            email = me.email,
            introduction = me.introduction,
            role = me.role,
            streak = me.streak,
            solvedToday = Random().nextBoolean(),
            createdAt = me.createdAt,
            updatedAt = me.updatedAt
        )
    }
}