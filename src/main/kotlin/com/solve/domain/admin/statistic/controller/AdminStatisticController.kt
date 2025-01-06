package com.solve.domain.admin.statistic.controller

import com.solve.domain.admin.statistic.service.AdminStatisticService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "관리자: 통계", description = "Admin: Statistic")
@RestController
@RequestMapping("/admin/statistics")
class AdminStatisticController(
    private val adminStatisticService: AdminStatisticService
)