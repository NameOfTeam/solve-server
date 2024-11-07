package com.solve.domain.workbook.service

import com.solve.domain.workbook.dto.request.CreateWorkbookRequest
import com.solve.domain.workbook.dto.request.UpdateWorkbookRequest
import com.solve.domain.workbook.dto.response.WorkbookResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface WorkbookService {
    fun getWorkbooks(pageable: Pageable): Page<WorkbookResponse>
    fun getWorkbook(workbookId: Long): WorkbookResponse
    fun createWorkbook(request: CreateWorkbookRequest): WorkbookResponse
    fun updateWorkbook(workbookId: Long, request: UpdateWorkbookRequest): WorkbookResponse
    fun deleteWorkbook(workbookId: Long)
}