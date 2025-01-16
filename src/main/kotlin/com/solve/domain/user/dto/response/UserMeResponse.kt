package com.solve.domain.user.dto.response

import com.solve.domain.user.domain.enums.UserRole
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class UserMeResponse(
    val id: UUID,
    val username: String,
    val email: String,
    val gender: String,
    val introduction: String? = null,
    val role: UserRole,
    val streak: Int,
    val maxStreak: Int,
    var grass: Map<LocalDate, Int>,
    val isSolvedToday: Boolean = false,
    val solvedCount: Int,
    val connections: List<UserMeConnectionResponse>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)