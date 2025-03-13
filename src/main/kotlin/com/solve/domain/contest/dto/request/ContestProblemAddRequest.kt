package com.solve.domain.contest.dto.request

data class ContestProblemAddRequest(
    val problemId: Long,
    val order: Int,
    val score: Int
)