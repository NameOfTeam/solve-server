package com.solve.domain.contest.dto.response

import com.solve.domain.contest.domain.entity.ContestOperator

data class ContestOperatorResponse(
    val username: String,
) {
    companion object {
        fun of(operator: ContestOperator) = ContestOperatorResponse(
            username = operator.user.username
        )
    }
}