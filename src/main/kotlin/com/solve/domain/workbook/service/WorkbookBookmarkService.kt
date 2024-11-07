package com.solve.domain.workbook.service

interface WorkbookBookmarkService {
    fun addWorkbookBookmark(workbookId: Long)
    fun removeWorkbookBookmark(workbookId: Long)
}