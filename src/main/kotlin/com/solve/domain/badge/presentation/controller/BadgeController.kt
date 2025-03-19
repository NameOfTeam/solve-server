package com.solve.domain.badge.presentation.controller

import com.solve.domain.badge.application.service.BadgeService
import com.solve.global.common.dto.BaseResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/badges")
class BadgeController(
    private val badgeService: BadgeService
) {
    @GetMapping
    fun getBadges() = BaseResponse.of(badgeService.getBadges())
}