package com.solve.domain.statistics.dto.response

data class StatisticsResponse(
    val problemCount: Long,
    val workbookCount: Long,
    val contestCount: Long,
    val userCount: Long
)