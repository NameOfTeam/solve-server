package com.solve.domain.workbook.controller

import com.solve.domain.workbook.dto.request.AddWorkbookProblemRequest
import com.solve.domain.workbook.service.WorkbookProblemService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "문제집 문제", description = "Workbook Problem")
@RestController
@RequestMapping("/workbooks/{workbookId}/problems")
class WorkbookProblemController(
    private val workbookProblemService: WorkbookProblemService
) {
    @Operation(summary = "문제집 문제 추가")
    @PostMapping
    fun addWorkbookProblem(@PathVariable workbookId: Long, @RequestBody request: AddWorkbookProblemRequest) =
        BaseResponse.of(workbookProblemService.addWorkbookProblem(workbookId, request))

    @Operation(summary = "문제집 문제 제거")
    @DeleteMapping("/{problemId}")
    fun removeWorkbookProblem(@PathVariable workbookId: Long, @PathVariable problemId: Long) =
        BaseResponse.of(workbookProblemService.removeWorkbookProblem(workbookId, problemId))
}