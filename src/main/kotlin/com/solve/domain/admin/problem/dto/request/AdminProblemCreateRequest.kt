package com.solve.domain.admin.problem.dto.request

data class AdminProblemCreateRequest(
    val title: String,
    val content: String,
    val input: String,
    val output: String,
    val memoryLimit: Long,
    val timeLimit: Double,
    val testCases: List<AdminProblemTestCaseCreateRequest>? = null
)
