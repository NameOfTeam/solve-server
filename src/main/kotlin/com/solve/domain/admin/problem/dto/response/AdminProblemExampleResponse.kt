package com.solve.domain.admin.problem.dto.response

import com.solve.domain.problem.domain.entity.ProblemExample

data class AdminProblemExampleResponse(
    val id: Long,
    val input: String,
    val output: String,
    val description: String?
) {
    companion object {
        fun of(example: ProblemExample) = AdminProblemExampleResponse(
            id = example.id!!,
            input = example.input,
            output = example.output,
            description = example.description
        )
    }
}