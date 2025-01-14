package com.solve.domain.contest.dto.response

import com.solve.domain.user.domain.entity.User
import java.util.UUID

data class ContestWinnerResponse(
    val id: UUID,
    val username: String
) {
    companion object {
        fun of(winner: User) = ContestWinnerResponse(
            id = winner.id!!,
            username = winner.username
        )
    }
}