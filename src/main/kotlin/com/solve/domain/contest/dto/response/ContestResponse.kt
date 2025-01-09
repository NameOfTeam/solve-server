package com.solve.domain.contest.dto.response

import com.solve.domain.contest.domain.entity.Contest
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
) {
    companion object {
        fun of(contest: Contest) = ContestResponse(
            id = contest.id!!,
            title = contest.title,
            description = contest.description,
            startAt = contest.startAt,
            endAt = contest.endAt,
            owner = ContestOwnerResponse.of(contest.owner),
            visibility = contest.visibility,
            state = if (contest.startAt.isAfter(LocalDateTime.now())) {
                ContestState.UPCOMING
            } else if (contest.endAt.isBefore(LocalDateTime.now())) {
                ContestState.ENDED
            } else {
                ContestState.ONGOING
            },
            winner = contest.winner?.let { ContestWinnerResponse.of(it) },
            operators = contest.operators.map { ContestOperatorResponse.of(it) },
            participants = contest.participants.map { ContestParticipantResponse.of(it) },
            problems = contest.problems.map { ContestProblemResponse.of(it) },
            createdAt = contest.createdAt,
            updatedAt = contest.updatedAt
        )
    }
}