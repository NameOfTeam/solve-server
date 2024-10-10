package com.solve.domain.problem.dto.request

data class ProblemTestCaseCreateRequest(
    val input: String,
    val output: String,
    val sample: Boolean
)