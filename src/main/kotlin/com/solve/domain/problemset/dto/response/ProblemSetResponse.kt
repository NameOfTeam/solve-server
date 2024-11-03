package com.solve.domain.problemset.dto.response

import com.solve.domain.problemset.domain.entity.ProblemSet

data class ProblemSetResponse(
    val title: String,
    val problems: List<ProblemSetProblemResponse>
) {
    companion object {
        fun of(problemSet: ProblemSet) = ProblemSetResponse(
            title = problemSet.title,
            problems = problemSet.problems.map { ProblemSetProblemResponse.of(it) }
        )
    }
}