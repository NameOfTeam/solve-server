package com.solve.domain.admin.workbook.service.impl

import com.solve.domain.admin.workbook.dto.request.AdminCreateWorkbookRequest
import com.solve.domain.admin.workbook.dto.response.AdminWorkbookResponse
import com.solve.domain.admin.workbook.service.AdminWorkbookService
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.error.WorkbookError
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
    private val problemRepository: ProblemRepository
) : AdminWorkbookService {
    @Transactional(readOnly = true)
    override fun getWorkbooks(pageable: Pageable): Page<AdminWorkbookResponse> {
        return workbookRepository.findAll(pageable).map { AdminWorkbookResponse.of(it) }
    }

    @Transactional(readOnly = true)
    override fun getWorkbook(workbookId: Long): AdminWorkbookResponse {
        return AdminWorkbookResponse.of(workbookRepository.findById(workbookId).get())
    }

    @Transactional
    override fun createWorkbook(request: AdminCreateWorkbookRequest): AdminWorkbookResponse {
        val author = securityHolder.user
        var workbook = Workbook(
            title = request.title,
            author = author,
        )

        request.problemIds.forEach {
            val problem = problemRepository.findByIdOrNull(it) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

            workbook.addProblem(problem)
        }

        workbook = workbookRepository.save(workbook)

        return AdminWorkbookResponse.of(workbook)
    }

    @Transactional
    override fun deleteWorkbook(workbookId: Long) {
        val workbook = workbookRepository.findByIdOrNull(workbookId) ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)

        workbookRepository.delete(workbook)
    }
}