package com.solve.domain.admin.workbook.service.impl

import com.solve.domain.admin.workbook.dto.request.AdminCreateWorkbookRequest
import com.solve.domain.admin.workbook.dto.response.AdminWorkbookAuthorResponse
import com.solve.domain.admin.workbook.dto.response.AdminWorkbookProblemResponse
import com.solve.domain.admin.workbook.dto.response.AdminWorkbookResponse
import com.solve.domain.admin.workbook.service.AdminWorkbookService
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.domain.entity.WorkbookProblem
import com.solve.domain.workbook.error.WorkbookError
import com.solve.domain.workbook.repository.WorkbookProblemRepository
import com.solve.domain.workbook.repository.WorkbookRepository
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminWorkbookServiceImpl(
    private val securityHolder: SecurityHolder,
    private val workbookRepository: WorkbookRepository,
    private val problemRepository: ProblemRepository,
    private val workbookProblemRepository: WorkbookProblemRepository
) : AdminWorkbookService {
    @Transactional(readOnly = true)
    override fun getWorkbooks(pageable: Pageable): Page<AdminWorkbookResponse> {
        return workbookRepository.findAll(pageable).map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun getWorkbook(workbookId: Long): AdminWorkbookResponse {
        val workbook =
            workbookRepository.findByIdOrNull(workbookId) ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)

        return workbook.toResponse()
    }

    @Transactional
    override fun createWorkbook(request: AdminCreateWorkbookRequest) {
        val author = securityHolder.user
        val workbook = Workbook(
            title = request.title,
            description = request.description,
            author = author,
        )

        workbookRepository.save(workbook)

        workbookProblemRepository.saveAll(request.problemIds.map {
            WorkbookProblem(
                workbook = workbook,
                problem = problemRepository.findByIdOrNull(it) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
            )
        })
    }

    @Transactional
    override fun deleteWorkbook(workbookId: Long) {
        val workbook =
            workbookRepository.findByIdOrNull(workbookId) ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)

        workbookRepository.delete(workbook)
    }

    private fun Workbook.toResponse() = AdminWorkbookResponse(
        id = id!!,
        title = title,
        problems = workbookProblemRepository.findAllByWorkbook(this).map { AdminWorkbookProblemResponse.of(it) },
        author = AdminWorkbookAuthorResponse.of(author),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}