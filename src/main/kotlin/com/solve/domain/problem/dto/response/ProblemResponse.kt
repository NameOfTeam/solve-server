package com.solve.domain.problem.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.solve.domain.problem.domain.entity.Problem

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProblemResponse(
    val id: Long,
    val title: String,
    val content: String,
    val input: String,
    val output: String,
    val memoryLimit: Long,
    val timeLimit: Long,
    var correctRate: Double? = null,
    val testCases: List<ProblemTestCaseResponse>
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
            testCases = problem.testCases.filter { it.sample }.map { ProblemTestCaseResponse.of(it) }
        )
    }
}