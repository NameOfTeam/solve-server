package com.solve.domain.problem.dto.response

import com.solve.domain.problem.domain.entity.ProblemIdea

data class ProblemIdeaResponse(
    val content: String
) {
    companion object {
        fun of(idea: ProblemIdea) = ProblemIdeaResponse(
            content = idea.content
        )
    }
}