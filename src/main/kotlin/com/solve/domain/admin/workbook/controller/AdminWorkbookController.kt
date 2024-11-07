package com.solve.domain.admin.workbook.controller

import com.solve.domain.admin.workbook.service.AdminWorkbookService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "관리자: 문제집", description = "Admin: Workbook")
@RestController
@RequestMapping("/admin/workbooks")
class AdminWorkbookController(
    private val adminWorkbookService: AdminWorkbookService
) {
    @Operation(summary = "문제집 목록 조회")
    @GetMapping
    fun getWorkbooks(@PageableDefault pageable: Pageable) =
        BaseResponse.of(adminWorkbookService.getWorkbooks(pageable))

    @Operation(summary = "문제집 상세 조회")
    @GetMapping("/{workbookId}")
    fun getWorkbook(@PathVariable workbookId: Long) =
        BaseResponse.of(adminWorkbookService.getWorkbook(workbookId))

    @Operation(summary = "문제집 삭제")
    @DeleteMapping("/{workbookId}")
    fun deleteWorkbook(@PathVariable workbookId: Long) =
        BaseResponse.of(adminWorkbookService.deleteWorkbook(workbookId))
}