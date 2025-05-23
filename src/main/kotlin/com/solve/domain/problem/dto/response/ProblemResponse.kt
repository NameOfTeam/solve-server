package com.solve.domain.problem.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.solve.domain.problem.domain.entity.ProblemExample
import com.solve.domain.problem.domain.enums.ProblemTag
import com.solve.domain.submit.domain.enums.SubmitState
import com.solve.domain.user.domain.entity.User
import com.solve.global.common.enums.Tier

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProblemResponse(
    val id: Long,
    val title: String,
    val content: String,
    val input: String,
    val output: String,
    val memoryLimit: Long,
    val timeLimit: Double,
    var correctRate: Double,
    val tier: Tier,
    val tags: Set<ProblemTag>,
    val solvedCount: Int,
    val examples: List<ProblemExampleResponse>,
    val author: ProblemAuthorResponse,
    var state: SubmitState? = null
)

data class ProblemAuthorResponse(
    val username: String
) {
    companion object {
        fun of(author: User) = ProblemAuthorResponse(
            username = author.username
        )
    }
}

data class ProblemExampleResponse(
    val input: String,
    val output: String,
    val description: String?
) {
    companion object {
        fun of(example: ProblemExample) = ProblemExampleResponse(
            input = example.input,
            output = example.output,
            description = example.description
        )
    }
}