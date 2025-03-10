package com.solve.domain.admin.workbook.service

import com.solve.domain.admin.workbook.dto.request.AdminCreateWorkbookRequest
import com.solve.domain.admin.workbook.dto.response.AdminWorkbookResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface AdminWorkbookService {
    fun getWorkbooks(pageable: Pageable): Page<AdminWorkbookResponse>
    fun getWorkbook(workbookId: Long): AdminWorkbookResponse
    fun createWorkbook(request: AdminCreateWorkbookRequest)
    fun deleteWorkbook(workbookId: Long)
}