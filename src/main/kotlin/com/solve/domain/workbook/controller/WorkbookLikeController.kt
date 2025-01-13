package com.solve.domain.workbook.controller

import com.solve.domain.workbook.service.WorkbookLikeService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "문제집 좋아요", description = "Workbook Like")
@RestController
@RequestMapping("/workbooks/{workbookId}/likes")
class WorkbookLikeController(
    private val workbookLikeService: WorkbookLikeService
) {
    @Operation(summary = "문제집 좋아요 추가")
    @PostMapping
    fun addWorkbookLike(@PathVariable workbookId: Long) =
        BaseResponse.of(workbookLikeService.addWorkbookLike(workbookId), 201)

    @Operation(summary = "문제집 좋아요 삭제")
    @DeleteMapping
    fun deleteWorkbookLike(@PathVariable workbookId: Long) =
        BaseResponse.of(workbookLikeService.removeWorkbookLike(workbookId), 204)
}