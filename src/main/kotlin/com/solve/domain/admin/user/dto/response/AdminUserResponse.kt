package com.solve.domain.admin.user.dto.response

import com.solve.domain.user.domain.enums.UserRole
import com.solve.domain.user.dto.response.UserMeConnectionResponse
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

data class AdminUserResponse(
    val id: UUID,
    val username: String,
    val email: String,
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