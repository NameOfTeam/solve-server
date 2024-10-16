package com.solve.domain.admin.contest.dto.response

import com.solve.domain.contest.domain.entity.ContestProblem

data class AdminContestProblemResponse(
    val title: String,
) {
    companion object {
        fun of(problem: ContestProblem) = AdminContestProblemResponse(
            title = problem.problem.title
        )
    }
}