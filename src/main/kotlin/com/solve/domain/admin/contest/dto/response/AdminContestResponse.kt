package com.solve.domain.admin.contest.dto.response

import com.solve.domain.contest.domain.entity.Contest
import java.time.LocalDateTime

data class AdminContestResponse(
    val id: Long,
    val title: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val participants: List<AdminContestParticipant>,
    val operators: List<AdminContestOperatorResponse>,
    val problems: List<AdminContestProblemResponse>
) {
    companion object {
        fun of(contest: Contest) = AdminContestResponse(
            id = contest.id!!,
            title = contest.title,
            description = contest.description,
            startTime = contest.startAt,
            endTime = contest.endAt,
            createdAt = contest.createdAt,
            updatedAt = contest.updatedAt,
            participants = contest.participants.map { AdminContestParticipant.of(it) },
            operators = contest.operators.map { AdminContestOperatorResponse.of(it) },
            problems = contest.problems.map { AdminContestProblemResponse.of(it) }
        )
    }
}