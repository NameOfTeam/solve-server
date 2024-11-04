package com.solve.domain.admin.contest.dto.response

import com.solve.domain.user.domain.entity.User
import java.util.*

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
