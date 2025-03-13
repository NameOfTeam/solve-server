package com.solve.domain.contest.presentation.dto.response

import com.solve.domain.contest.domain.entity.ContestProblem

data class ContestProblemResponse(
    val title: String
) {
    companion object {
        fun of(problem: ContestProblem) = ContestProblemResponse(
            title = problem.problem.title
        )
    }
}