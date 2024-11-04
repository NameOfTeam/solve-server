package com.solve.domain.admin.contest.dto.response

import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.enums.ContestVisibility
import java.time.LocalDateTime

data class AdminContestResponse(
    val id: Long,
    val title: String,
    val description: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val owner: AdminContestOwnerResponse,
    val participants: List<AdminContestParticipant>,
    val operators: List<AdminContestOperatorResponse>,
    val problems: List<AdminContestProblemResponse>,
    val visibility: ContestVisibility,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun of(contest: Contest) = AdminContestResponse(
            id = contest.id!!,
            title = contest.title,
            description = contest.description,
            startAt = contest.startAt,
            endAt = contest.endAt,
            createdAt = contest.createdAt,
            updatedAt = contest.updatedAt,
            participants = contest.participants.map { AdminContestParticipant.of(it) },
            operators = contest.operators.map { AdminContestOperatorResponse.of(it) },
            problems = contest.problems.map { AdminContestProblemResponse.of(it) },
            visibility = contest.visibility,
            owner = AdminContestOwnerResponse.of(contest.owner)
        )
    }
}