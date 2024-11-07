package com.solve.domain.workbook.service

import com.solve.domain.workbook.dto.request.AddWorkbookProblemRequest

interface WorkbookProblemService {
    fun addWorkbookProblem(workbookId: Long, request: AddWorkbookProblemRequest)
    fun removeWorkbookProblem(workbookId: Long, problemId: Long)
}