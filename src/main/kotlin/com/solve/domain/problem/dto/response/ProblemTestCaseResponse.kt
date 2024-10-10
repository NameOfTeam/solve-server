package com.solve.domain.problem.dto.response

import com.solve.domain.problem.domain.entity.ProblemTestCase

data class ProblemTestCaseResponse(
    val id: Long,
    val input: List<String>,
    val output: List<String>,
) {
    companion object {
        fun of(testcase: ProblemTestCase) = ProblemTestCaseResponse(
            id = testcase.id!!,
            input = testcase.input,
            output = testcase.output,
        )
    }
}