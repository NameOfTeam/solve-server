package com.solve.domain.badge.application.service

import com.solve.domain.badge.domain.entity.Badge
import com.solve.domain.badge.domain.repository.BadgeRepository
import com.solve.domain.badge.presentation.dto.response.BadgeResponse
import org.springframework.stereotype.Service

@Service
class BadgeService(
    private val badgeRepository: BadgeRepository
) {
    fun getBadges(): List<BadgeResponse> {
        val badges = badgeRepository.findAll()

        return badges.map { it.toResponse() }
    }

    private fun Badge.toResponse() = BadgeResponse(
        id = id!!,
        name = name,
        description = description,
        imageUrl = imageUrl,
        condition = condition
    )
}