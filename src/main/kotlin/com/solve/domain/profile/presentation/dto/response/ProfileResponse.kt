package com.solve.domain.profile.presentation.dto.response

import com.solve.global.common.enums.ProgrammingLanguage
import com.solve.global.common.enums.Tier
import java.time.LocalDate
import java.util.UUID

data class ProfileResponse(
    val id: UUID,
    val introduction: String?,
    val badges: List<ProfileBadgeResponse>,
    val tier: Tier,
    val rating: Int,
    val solvedCount: Int,
    val contestParticipationCount: Long,
    val longestStreak: Int,
    val submissionCount: Long,
    val languages: List<ProfileLanguageResponse>,
    val gender: String?,
    val birth: LocalDate?,
    val links: List<String>
)

data class ProfileBadgeResponse(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val condition: String
)

data class ProfileLanguageResponse(
    val language: ProgrammingLanguage,
    val solvedCount: Int,
)