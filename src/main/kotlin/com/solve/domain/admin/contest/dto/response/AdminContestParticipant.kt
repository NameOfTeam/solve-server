package com.solve.domain.admin.contest.dto.response

import com.solve.domain.contest.domain.entity.ContestParticipant
import java.util.*

data class AdminContestParticipant(
    val id: UUID,
    val username: String
) {
    companion object {
        fun of(participant: ContestParticipant) = AdminContestParticipant(
            id = participant.user.id!!,
            username = participant.user.username
        )
    }
}