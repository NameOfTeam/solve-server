package com.solve.domain.admin.problem.dto.request

import com.solve.global.common.enums.Tier

data class AdminProblemCreateRequest(
    val title: String,
    val content: String,
    val input: String,
    val output: String,
    val memoryLimit: Long,
    val tier: Tier,
    val timeLimit: Double,
    val testCases: List<AdminProblemTestCaseCreateRequest>? = null
)
