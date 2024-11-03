package com.solve.domain.admin.contest.dto.request

import com.solve.domain.contest.domain.enums.ContestVisibility
import java.time.LocalDateTime

data class AdminContestUpdateRequest(
    val title: String?,
    val description: String?,
    val startAt: LocalDateTime?,
    val endAt: LocalDateTime?,
    val visibility: ContestVisibility?,
)