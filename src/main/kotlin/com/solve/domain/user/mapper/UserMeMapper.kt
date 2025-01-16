package com.solve.domain.user.mapper

import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.dto.response.UserMeConnectionResponse
import com.solve.domain.user.dto.response.UserMeResponse
import com.solve.domain.user.repository.UserConnectionRepository
import org.springframework.stereotype.Component

@Component
class UserMeMapper(
    private val userConnectionRepository: UserConnectionRepository
) {
    fun toResponse(me: User) = UserMeResponse(
        id = me.id!!,
        username = me.username,
        email = me.email,
        gender = me.gender,
        introduction = me.introduction,
        role = me.role,
        streak = me.streak,
        maxStreak = me.maxStreak,
        grass = me.grass,
        solvedCount = me.solvedCount,
        isSolvedToday = me.solvedToday,
        connections = userConnectionRepository.findAllByUser(me).map { UserMeConnectionResponse.of(it) },
        createdAt = me.createdAt,
        updatedAt = me.updatedAt
    )
}