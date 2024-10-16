package com.solve.domain.admin.contest.dto.response

import com.solve.domain.contest.domain.entity.ContestOperator
import java.util.*

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