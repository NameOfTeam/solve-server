package com.solve.domain.contest.dto.response

import com.solve.domain.contest.domain.entity.Contest
import java.time.LocalDateTime

data class ContestResponse(
    val id: Long,
    val title: String,
    val description: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val operators: List<ContestOperatorResponse>,
    val participants: List<ContestParticipantResponse>,
    val problems: List<ContestProblemResponse>,
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
            operators = contest.operators.map { ContestOperatorResponse.of(it) },
            participants = contest.participants.map { ContestParticipantResponse.of(it) },
            problems = contest.problems.map { ContestProblemResponse.of(it) },
            createdAt = contest.createdAt,
            updatedAt = contest.updatedAt
        )
    }
}