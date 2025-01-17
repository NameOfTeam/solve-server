package com.solve.domain.theme.controller

import com.solve.domain.theme.service.ThemeSearchService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "테마 검색", description = "Theme Search")
@RestController
@RequestMapping("/themes/search")
class ThemeSearchController(
    private val themeSearchService: ThemeSearchService
) {
    @Operation(summary = "테마 검색")
    @GetMapping
    fun searchTheme(
        @RequestParam(required = false, defaultValue = "") query: String,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "10") size: Int
    ) = BaseResponse.of(themeSearchService.searchTheme(query, PageRequest.of(page, size)))
}