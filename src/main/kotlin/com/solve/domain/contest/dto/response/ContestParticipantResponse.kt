package com.solve.domain.contest.dto.response

import com.solve.domain.contest.domain.entity.ContestParticipant

data class ContestParticipantResponse(
    val username: String,
) {
    companion object {
        fun of(participant: ContestParticipant) = ContestParticipantResponse(
            username = participant.user.username
        )
    }
}