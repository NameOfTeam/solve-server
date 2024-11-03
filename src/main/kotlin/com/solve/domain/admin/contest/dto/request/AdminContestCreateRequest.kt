package com.solve.domain.admin.contest.dto.request

import com.solve.domain.contest.domain.enums.ContestVisibility
import java.time.LocalDateTime
import java.util.*

data class AdminContestCreateRequest(
    val title: String,
    val description: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val visibility: ContestVisibility,
    val operatorIds: List<UUID>,
    val participantIds: List<UUID>,
    val problemIds: List<Long>,
)