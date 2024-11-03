package com.solve.domain.problemset.dto.response

import com.solve.domain.problemset.domain.entity.ProblemSetProblem

data class ProblemSetProblemResponse(
    val title: String,
    val content: String,
    val input: String,
    val output: String,
    val memoryLimit: Long,
    val timeLimit: Double,
) {
    companion object {
        fun of(problem: ProblemSetProblem) = ProblemSetProblemResponse(
            title = problem.problem.title,
            content = problem.problem.content,
            input = problem.problem.input,
            output = problem.problem.output,
            memoryLimit = problem.problem.memoryLimit,
            timeLimit = problem.problem.timeLimit
        )
    }
}