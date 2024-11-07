package com.solve.domain.admin.workbook.service.impl

import com.solve.domain.admin.workbook.dto.response.AdminWorkbookResponse
import com.solve.domain.admin.workbook.service.AdminWorkbookService
import com.solve.domain.workbook.repository.WorkbookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminWorkbookServiceImpl(
    private val workbookRepository: WorkbookRepository
) : AdminWorkbookService {
    @Transactional(readOnly = true)
    override fun getWorkbooks(pageable: Pageable): Page<AdminWorkbookResponse> {
        return workbookRepository.findAll(pageable).map { AdminWorkbookResponse.of(it) }
    }

    @Transactional(readOnly = true)
    override fun getWorkbook(workbookId: Long): AdminWorkbookResponse {
        return AdminWorkbookResponse.of(workbookRepository.findById(workbookId).get())
    }
}