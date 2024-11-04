package com.solve.domain.admin.contest.dto.request

data class AdminContestProblemAddRequest(
    val problemId: Long,
    val score: Int
)