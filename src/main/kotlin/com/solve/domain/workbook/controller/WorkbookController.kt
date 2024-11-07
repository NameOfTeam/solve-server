package com.solve.domain.workbook.controller

import com.solve.domain.workbook.dto.request.CreateWorkbookRequest
import com.solve.domain.workbook.dto.request.UpdateWorkbookRequest
import com.solve.domain.workbook.service.WorkbookService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@Tag(name = "문제집", description = "Workbook")
@RestController
@RequestMapping("/workbooks")
class WorkbookController(
    private val workbookService: WorkbookService
) {
    @Operation(summary = "문제집 목록 조회")
    @GetMapping
    fun getWorkbooks(@PageableDefault pageable: Pageable) =
        BaseResponse.of(workbookService.getWorkbooks(pageable))

    @Operation(summary = "문제집 상세 조회")
    @GetMapping("/{workbookId}")
    fun getWorkbook(@PathVariable workbookId: Long) = BaseResponse.of(workbookService.getWorkbook(workbookId))

    @Operation(summary = "문제집 생성")
    @PostMapping
    fun createWorkbook(@RequestBody request: CreateWorkbookRequest) =
        BaseResponse.of(workbookService.createWorkbook(request))

    @Operation(summary = "문제집 수정")
    @PatchMapping("/{workbookId}")
    fun updateWorkbook(@PathVariable workbookId: Long, @RequestBody request: UpdateWorkbookRequest) =
        BaseResponse.of(workbookService.updateWorkbook(workbookId, request))

    @Operation(summary = "문제집 삭제")
    @DeleteMapping("/{workbookId}")
    fun deleteWorkbook(@PathVariable workbookId: Long) =
        BaseResponse.of(workbookService.deleteWorkbook(workbookId))
}