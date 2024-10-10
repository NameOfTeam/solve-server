package com.solve.domain.problem.dto.request

data class ProblemTestCaseUpdateRequest(
    val input: String?,
    val output: String?,
    val sample: Boolean?,
)