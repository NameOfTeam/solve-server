package com.solve.domain.problem.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.user.domain.entity.User

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
    val testCases: List<ProblemTestCaseResponse>,
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
            testCases = problem.testCases.filter { it.sample }.map { ProblemTestCaseResponse.of(it) },
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