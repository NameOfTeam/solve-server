package com.solve.domain.contest.dto.response

import com.solve.domain.contest.domain.entity.Contest

data class ContestResponse(
    val id: Long,
) {
    companion object {
        fun of(contest: Contest) = ContestResponse(
            id = contest.id!!,
        )
    }
}