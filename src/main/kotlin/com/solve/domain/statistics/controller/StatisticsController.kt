package com.solve.domain.statistics.controller

import com.solve.domain.statistics.service.StatisticsService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "통계", description = "Statistics")
@RestController
@RequestMapping("/statistics")
class StatisticsController(
    private val statisticsService: StatisticsService
) {
    @Operation(summary = "통계")
    @GetMapping
    fun getStatistics() = BaseResponse.of(statisticsService.getStatistics())
}