package com.solve.domain.contest.presentation.dto.response

import com.solve.domain.contest.domain.enums.ContestState
import java.time.LocalDateTime

data class ContestResponse(
    val id: Long,
    val title: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val owner: ContestOwnerResponse,
    val state: ContestState,
    val winner: ContestWinnerResponse?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)