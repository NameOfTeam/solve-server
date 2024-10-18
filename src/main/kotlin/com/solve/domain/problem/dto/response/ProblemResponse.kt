package com.solve.domain.problem.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemSubmit
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
    var correctRate: Double? = null,
    val testCases: List<ProblemTestCaseResponse>,
    val author: ProblemAuthorResponse,
    val state: ProblemSubmitState? = null
) {
    fun correctRate(submits: List<ProblemSubmit>) {
        correctRate = if (submits.isEmpty()) {
            0.0
        } else {
            val correctCount = submits.count { it.state == ProblemSubmitState.ACCEPTED }
            (correctCount.toDouble() / submits.size * 1000).toInt() / 10.0
        }
    }

    companion object {
        fun of(problem: Problem, state: ProblemSubmitState? = null) = ProblemResponse(
            id = problem.id!!,
            title = problem.title,
            content = problem.content,
            input = problem.input,
            output = problem.output,
            memoryLimit = problem.memoryLimit,
            timeLimit = problem.timeLimit,
            testCases = problem.testCases.filter { it.sample }.map { ProblemTestCaseResponse.of(it) },
            author = ProblemAuthorResponse.of(problem.author),
            state = state
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