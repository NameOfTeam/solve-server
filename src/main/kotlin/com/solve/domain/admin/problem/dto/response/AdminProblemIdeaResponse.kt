package com.solve.domain.admin.problem.dto.response

import com.solve.domain.problem.domain.entity.ProblemIdea

data class AdminProblemIdeaResponse(
    val id: Long,
    val content: String
) {
    companion object {
        fun of(idea: ProblemIdea) = AdminProblemIdeaResponse(
            id = idea.id!!,
            content = idea.content
        )
    }
}