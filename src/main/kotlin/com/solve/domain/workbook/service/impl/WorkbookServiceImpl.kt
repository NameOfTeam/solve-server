package com.solve.domain.workbook.service.impl

import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.domain.entity.WorkbookProblem
import com.solve.domain.workbook.dto.request.CreateWorkbookRequest
import com.solve.domain.workbook.dto.request.UpdateWorkbookRequest
import com.solve.domain.workbook.dto.response.WorkbookAuthorResponse
import com.solve.domain.workbook.dto.response.WorkbookProblemResponse
import com.solve.domain.workbook.dto.response.WorkbookResponse
import com.solve.domain.workbook.error.WorkbookError
import com.solve.domain.workbook.repository.WorkbookProblemRepository
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
    private val workbookRepository: WorkbookRepository,
    private val problemRepository: ProblemRepository,
    private val workbookProblemRepository: WorkbookProblemRepository
) : WorkbookService {
    @Transactional(readOnly = true)
    override fun getWorkbooks(pageable: Pageable): Page<WorkbookResponse> {
        return workbookRepository.findAll(pageable).map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun getWorkbook(workbookId: Long): WorkbookResponse {
        val workbook = workbookRepository.findByIdOrNull(workbookId)
            ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)

        return workbook.toResponse()
    }

    @Transactional
    override fun createWorkbook(request: CreateWorkbookRequest): WorkbookResponse {
        val author = securityHolder.user
        val workbook = workbookRepository.save(
            Workbook(
                title = request.title,
                description = request.description,
                author = author
            )
        )

        request.problemIds.forEach {
            val problem = problemRepository.findByIdOrNull(it) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

            workbookProblemRepository.save(
                WorkbookProblem(
                    workbook = workbook,
                    problem = problem
                )
            )
        }

        return workbook.toResponse()
    }

    @Transactional
    override fun updateWorkbook(workbookId: Long, request: UpdateWorkbookRequest): WorkbookResponse {
        var workbook = workbookRepository.findByIdOrNull(workbookId)
            ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)

        if (request.title != null) workbook.title = request.title

        workbook = workbookRepository.save(workbook)

        return workbook.toResponse()
    }

    @Transactional
    override fun deleteWorkbook(workbookId: Long) {
        val workbook = workbookRepository.findByIdOrNull(workbookId)
            ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)

        workbookRepository.delete(workbook)
    }

    private fun Workbook.toResponse() = WorkbookResponse(
        id = id!!,
        title = title,
        description = description,
        problems = workbookProblemRepository.findAllByWorkbook(this).map { WorkbookProblemResponse.of(it) },
        author = WorkbookAuthorResponse.of(author),
        likeCount = likes.size.toLong(),
        bookmarkCount = bookmarks.size.toLong(),
        createdAt = createdAt,
        updatedAt = updatedAt
    ).apply {
        if (securityHolder.isAuthenticated) {
            val user = securityHolder.user

            progress = this.problems.intersect(user.solved.map { it.problem }.toSet()).size
            isLiked = likes.any { it.user == user }
            isBookmarked = bookmarks.any { it.user == user }
        }
    }
}