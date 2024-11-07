package com.solve.domain.workbook.service.impl

import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.dto.request.CreateWorkbookRequest
import com.solve.domain.workbook.dto.request.UpdateWorkbookRequest
import com.solve.domain.workbook.dto.response.WorkbookResponse
import com.solve.domain.workbook.error.WorkbookError
import com.solve.domain.workbook.repository.WorkbookRepository
import com.solve.domain.workbook.service.WorkbookService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkbookServiceImpl(
    private val securityHolder: SecurityHolder,
    private val workbookRepository: WorkbookRepository
) : WorkbookService {
    @Transactional(readOnly = true)
    override fun getWorkbooks(pageable: Pageable): Page<WorkbookResponse> {
        return workbookRepository.findAll(pageable).map { WorkbookResponse.of(it) }
    }

    @Transactional(readOnly = true)
    override fun getWorkbook(workbookId: Long): WorkbookResponse {
        val workbook = workbookRepository.findByIdOrNull(workbookId)
            ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)

        return WorkbookResponse.of(workbook)
    }

    @Transactional
    override fun createWorkbook(request: CreateWorkbookRequest): WorkbookResponse {
        val author = securityHolder.user
        val workbook = workbookRepository.save(
            Workbook(
                title = request.title,
                author = author
            )
        )

        return WorkbookResponse.of(workbook)
    }

    @Transactional
    override fun updateWorkbook(workbookId: Long, request: UpdateWorkbookRequest): WorkbookResponse {
        var workbook = workbookRepository.findByIdOrNull(workbookId)
            ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)

        if (request.title != null) workbook.title = request.title

        workbook = workbookRepository.save(workbook)

        return WorkbookResponse.of(workbook)
    }

    @Transactional
    override fun deleteWorkbook(workbookId: Long) {
        val workbook = workbookRepository.findByIdOrNull(workbookId)
            ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)

        workbookRepository.delete(workbook)
    }
}