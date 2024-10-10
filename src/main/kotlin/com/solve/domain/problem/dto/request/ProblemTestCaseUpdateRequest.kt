package com.solve.domain.problem.dto.request

data class ProblemTestCaseUpdateRequest(
    val input: List<String>?,
    val output: List<String>?,
    val sample: Boolean?,
)