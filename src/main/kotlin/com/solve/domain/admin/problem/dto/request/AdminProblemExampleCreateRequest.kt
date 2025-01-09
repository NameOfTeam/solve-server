package com.solve.domain.admin.problem.dto.request

data class AdminProblemExampleCreateRequest(
    val input: String,
    val output: String,
    val description: String?
)