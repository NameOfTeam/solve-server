package com.solve.domain.workbook.controller

import com.solve.domain.workbook.service.WorkbookBookmarkService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "북마크", description = "Workbook Bookmark")
@RestController
@RequestMapping("/workbooks/{workbookId}/bookmarks")
class WorkbookBookmarkController(
    private val workbookBookmarkService: WorkbookBookmarkService
) {
    @Operation(summary = "북마크 추가")
    @PostMapping
    fun addWorkbookBookmark(@PathVariable workbookId: Long) = BaseResponse.of(workbookBookmarkService.addWorkbookBookmark(workbookId), 201)

    @Operation(summary = "북마크 삭제")
    @DeleteMapping
    fun removeWorkbookBookmark(@PathVariable workbookId: Long) = BaseResponse.of(workbookBookmarkService.removeWorkbookBookmark(workbookId), 204)
}