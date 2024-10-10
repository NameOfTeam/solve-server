package com.solve.domain.problem.dto.response

import com.solve.domain.problem.domain.entity.Problem

data class ProblemResponse(
    val id: Long,
    val title: String,
    val content: String,
) {
    companion object {
        fun of(problem: Problem) = ProblemResponse(
            id = problem.id!!,
            title = problem.title,
            content = problem.content,
        )
    }
}