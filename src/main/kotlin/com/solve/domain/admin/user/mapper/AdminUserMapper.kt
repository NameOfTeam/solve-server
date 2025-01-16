package com.solve.domain.admin.user.mapper

import com.solve.domain.admin.user.dto.response.AdminUserResponse
import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.dto.response.UserMeConnectionResponse
import com.solve.domain.user.repository.UserConnectionRepository
import org.springframework.stereotype.Component

@Component
class AdminUserMapper(
    private val userConnectionRepository: UserConnectionRepository
) {
    fun toResponse(user: User) = AdminUserResponse(
        id = user.id!!,
        username = user.username,
        email = user.email,
        introduction = user.introduction,
        role = user.role,
        streak = user.streak,
        maxStreak = user.maxStreak,
        grass = user.grass,
        solvedCount = user.solvedCount,
        isSolvedToday = user.solvedToday,
        connections = userConnectionRepository.findAllByUser(user).map { UserMeConnectionResponse.of(it) },
        createdAt = user.createdAt,
        updatedAt = user.updatedAt
    )
}