package com.solve.domain.workbook.service

interface WorkbookLikeService {
    fun addWorkbookLike(workbookId: Long)
    fun removeWorkbookLike(workbookId: Long)
}