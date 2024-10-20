package com.solve.domain.contest.dto.response

import com.solve.domain.user.domain.entity.User

data class ContestOwnerResponse(
    val username: String
) {
    companion object {
        fun of(owner: User) = ContestOwnerResponse(
            username = owner.username
        )
    }
}