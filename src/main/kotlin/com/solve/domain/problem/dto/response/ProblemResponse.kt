package com.solve.domain.problem.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemExample
import com.solve.domain.problem.domain.enums.ProblemSubmitState
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
    val solvedCount: Int,
    val examples: List<ProblemExampleResponse>,
    val author: ProblemAuthorResponse,
    var state: ProblemSubmitState? = null
) {
    companion object {
        fun of(problem: Problem) = ProblemResponse(
            id = problem.id!!,
            title = problem.title,
            content = problem.content,
            input = problem.input,
            output = problem.output,
            memoryLimit = problem.memoryLimit,
            timeLimit = problem.timeLimit,
            tier = problem.tier,
            solvedCount = problem.submits.filter { it.state == ProblemSubmitState.ACCEPTED }.distinctBy { it.author }
                .count(),
            examples = problem.examples.map { ProblemExampleResponse.of(it) },
            author = ProblemAuthorResponse.of(problem.author),
            correctRate = (problem.submits.map { it.state }
                .filter { it == ProblemSubmitState.ACCEPTED }.size.toDouble() / problem.submits.size * 1000).toInt() / 10.0,
        )
    }
}

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