package com.solve.domain.admin.problemset.dto.response

import com.solve.domain.problemset.domain.entity.ProblemSet

data class AdminProblemSetResponse(
    val id: Long
) {
    companion object {
        fun of(problemSet: ProblemSet) = AdminProblemSetResponse(
            id = problemSet.id!!
        )
    }
}