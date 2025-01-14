package com.solve.domain.theme.controller

import com.solve.domain.theme.service.ThemeService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "테마", description = "Theme")
@RestController
@RequestMapping("/themes")
class ThemeController(
    private val themeService: ThemeService
) {
    @Operation(summary = "테마 목록 조회")
    @GetMapping
    fun getThemes(
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "10") size: Int
    ) = BaseResponse.of(themeService.getThemes(PageRequest.of(page, size)))
}