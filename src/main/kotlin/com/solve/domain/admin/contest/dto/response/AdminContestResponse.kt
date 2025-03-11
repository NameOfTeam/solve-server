package com.solve.domain.admin.contest.dto.response

import com.solve.domain.contest.domain.entity.ContestOperator
import com.solve.domain.contest.domain.entity.ContestParticipant
import com.solve.domain.contest.domain.entity.ContestProblem
import com.solve.domain.user.domain.entity.User
import java.time.LocalDateTime
import java.util.*

data class AdminContestResponse(
    val id: Long,
    val title: String,
    val description: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val owner: AdminContestOwnerResponse,
    val participants: List<AdminContestParticipantResponse>,
    val operators: List<AdminContestOperatorResponse>,
    val problems: List<AdminContestProblemResponse>,
    val isPublic: Boolean,
    val isDeleted: Boolean,
    val isRegistrationOpen: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class AdminContestProblemResponse(
    val title: String,
) {
    companion object {
        fun of(problem: ContestProblem) = AdminContestProblemResponse(
            title = problem.problem.title
        )
    }
}

data class AdminContestParticipantResponse(
    val id: UUID,
    val username: String
) {
    companion object {
        fun of(participant: ContestParticipant) = AdminContestParticipantResponse(
            id = participant.user.id!!,
            username = participant.user.username
        )
    }
}

data class AdminContestOwnerResponse(
    val id: UUID,
    val username: String
) {
    companion object {
        fun of(owner: User) = AdminContestOwnerResponse(
            id = owner.id!!,
            username = owner.username
        )
    }
}

data class AdminContestOperatorResponse(
    val id: UUID,
    val username: String,
) {
    companion object {
        fun of(operator: ContestOperator) = AdminContestOperatorResponse(
            id = operator.user.id!!,
            username = operator.user.username
        )
    }
}