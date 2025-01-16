package com.solve.domain.user.dto.response

import com.solve.domain.user.domain.entity.UserConnection
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class UserResponse(
    val id: UUID,
    val username: String,
    val email: String,
    val gender: String,
    val introduction: String?,
    val streak: Int,
    val maxStreak: Int,
    val grass: Map<LocalDate, Int>,
    val isSolvedToday: Boolean,
    val solvedCount: Int,
    val connections: List<UserConnectionResponse>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class UserConnectionResponse(
    val type: String,
    val value: String
) {
    companion object {
        fun of(connection: UserConnection) = UserConnectionResponse(
            type = connection.type.name,
            value = connection.value
        )
    }
}