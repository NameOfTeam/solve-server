package com.solve.domain.problem.dto.request

data class ProblemCreateRequest(
    val title: String,
    val content: String,
    val input: String,
    val output: String,
    val memoryLimit: Long,
    val timeLimit: Long,
)
