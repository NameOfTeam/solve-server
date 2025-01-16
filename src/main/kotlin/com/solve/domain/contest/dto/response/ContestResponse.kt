package com.solve.domain.contest.dto.response

import com.solve.domain.contest.domain.enums.ContestState
import com.solve.domain.contest.domain.enums.ContestVisibility
import java.time.LocalDateTime

data class ContestResponse(
    val id: Long,
    val title: String,
    val description: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val owner: ContestOwnerResponse,
    val state: ContestState,
    val winner: ContestWinnerResponse?,
    val operators: List<ContestOperatorResponse>,
    val participants: List<ContestParticipantResponse>,
    val problems: List<ContestProblemResponse>,
    val visibility: ContestVisibility,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)