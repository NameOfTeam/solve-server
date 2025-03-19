package com.solve.domain.profile.presentation.dto.response

import com.solve.global.common.enums.Tier
import java.time.LocalDate

data class ProfileSummaryResponse(
    val streak: Int,
    val grass: Map<LocalDate, Int>,
    val tier: Tier,
    val rating: Int,
    val problems: List<ProfileSummaryProblemResponse>,
    val categorySolvedCount: Map<String, Int>,
    val tierSolvedCount: Map<String, Int>
)

data class ProfileSummaryProblemResponse(
    val tier: Tier,
    val title: String,
)