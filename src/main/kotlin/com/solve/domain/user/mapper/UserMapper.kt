package com.solve.domain.user.mapper

import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.dto.response.UserConnectionResponse
import com.solve.domain.user.dto.response.UserResponse
import com.solve.domain.user.repository.UserConnectionRepository
import org.springframework.stereotype.Component

@Component
class UserMapper(
    private val userConnectionRepository: UserConnectionRepository
) {
    fun toResponse(user: User) = UserResponse(
        id = user.id!!,
        username = user.username,
        email = user.email,
        gender = user.gender,
        introduction = user.introduction,
        streak = user.streak,
        maxStreak = user.maxStreak,
        grass = user.grass,
        isSolvedToday = user.solvedToday,
        solvedCount = user.solvedCount,
        connections = userConnectionRepository.findAllByUser(user).map { UserConnectionResponse.of(it) },
        createdAt = user.createdAt,
        updatedAt = user.updatedAt
    )
}