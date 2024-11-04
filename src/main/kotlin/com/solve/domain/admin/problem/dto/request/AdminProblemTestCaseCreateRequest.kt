package com.solve.domain.admin.problem.dto.request

data class AdminProblemTestCaseCreateRequest(
    val input: String,
    val output: String,
    val sample: Boolean
)