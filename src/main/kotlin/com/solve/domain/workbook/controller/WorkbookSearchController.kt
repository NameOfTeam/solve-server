package com.solve.domain.workbook.controller

import com.solve.domain.workbook.domain.enums.WorkbookSearchFilter
import com.solve.domain.workbook.service.WorkbookSearchService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "문제집 검색", description = "Workbook Search")
@RestController
@RequestMapping("/workbooks/search")
class WorkbookSearchController(
    private val workbookSearchService: WorkbookSearchService
) {
    @Operation(summary = "문제집 검색")
    @GetMapping
    fun searchWorkbook(
        @RequestParam(required = false, defaultValue = "") query: String,
        @RequestParam(required = false) filter: WorkbookSearchFilter?,
        @PageableDefault pageable: Pageable
    ) = BaseResponse.of(workbookSearchService.searchWorkbook(query, filter, pageable))
}