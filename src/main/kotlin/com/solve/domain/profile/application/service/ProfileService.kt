package com.solve.domain.profile.application.service

import com.solve.domain.contest.domain.repository.ContestParticipantRepository
import com.solve.domain.profile.presentation.dto.response.ProfileBadgeResponse
import com.solve.domain.profile.presentation.dto.response.ProfileResponse
import com.solve.domain.submit.repository.SubmitRepository
import com.solve.domain.user.domain.entity.UserBadge
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserBadgeRepository
import com.solve.domain.user.repository.UserLinkRepository
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileService(
    private val securityHolder: SecurityHolder,
    private val userRepository: UserRepository,
    private val userLinkRepository: UserLinkRepository,
    private val userBadgeRepository: UserBadgeRepository,
    private val submitRepository: SubmitRepository,
    private val contestParticipantRepository: ContestParticipantRepository
) {
    @Transactional(readOnly = true)
    fun getProfile(username: String? = null): ProfileResponse {
        val user = if (username == null) securityHolder.user else userRepository.findByUsername(username) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_USERNAME, username)
        val links = userLinkRepository.findAllByUser(user)
        val badges = userBadgeRepository.findAllByUser(user)
        val submissionCount = submitRepository.countByAuthor(user)
        val contestParticipantCount = contestParticipantRepository.countByUser(user)

        val profile = ProfileResponse(
            id = user.id!!,
            introduction = user.introduction,
            tier = user.tier,
            birth = user.birth,
            gender = user.gender,
            links = links.map { it.link },
            badges = badges.map { it.toResponse() },
            longestStreak = user.maxStreak,
            solvedCount = user.solvedCount,
            rating = user.rating,
            languages = emptyList(),
            submissionCount = submissionCount,
            contestParticipationCount = contestParticipantCount
        )

        return profile
    }

    private fun UserBadge.toResponse() = ProfileBadgeResponse(
        id = badge.id!!,
        name = badge.name,
        description = badge.description,
        imageUrl = badge.imageUrl,
        condition = badge.condition
    )
}