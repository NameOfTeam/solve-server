package com.solve.domain.admin.contest.dto.request

import java.time.LocalDateTime

data class AdminContestUpdateRequest(
    val title: String?,
    val description: String?,
    val startTime: LocalDateTime?,
    val endTime: LocalDateTime?,
    val isPublic: Boolean?,
)