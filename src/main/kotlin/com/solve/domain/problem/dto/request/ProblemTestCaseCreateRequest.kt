package com.solve.domain.problem.dto.request

data class ProblemTestCaseCreateRequest(
    val input: List<String>,
    val output: List<String>,
    val sample: Boolean
)