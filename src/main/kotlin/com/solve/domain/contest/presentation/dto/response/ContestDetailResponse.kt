package com.solve.domain.contest.presentation.dto.response

import com.solve.domain.contest.domain.enums.ContestState
import java.time.LocalDateTime

data class ContestDetailResponse(
    val id: Long,
    val title: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val owner: ContestOwnerResponse,
    val state: ContestState,
    val winner: ContestWinnerResponse?,
    val operators: List<ContestOperatorResponse>,
    val participants: List<ContestParticipantResponse>,
    val problems: List<ContestProblemResponse>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)