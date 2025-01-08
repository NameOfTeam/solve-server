package com.solve.domain.statistics.service

import com.solve.domain.statistics.dto.response.StatisticsResponse

interface StatisticsService {
    fun getStatistics(): StatisticsResponse
}