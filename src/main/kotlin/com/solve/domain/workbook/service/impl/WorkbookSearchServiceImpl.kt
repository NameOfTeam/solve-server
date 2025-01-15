package com.solve.domain.workbook.service.impl

import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.domain.enums.WorkbookSearchFilter
import com.solve.domain.workbook.dto.response.WorkbookAuthorResponse
import com.solve.domain.workbook.dto.response.WorkbookProblemResponse
import com.solve.domain.workbook.dto.response.WorkbookResponse
import com.solve.domain.workbook.repository.WorkbookBookmarkRepository
import com.solve.domain.workbook.repository.WorkbookLikeRepository
import com.solve.domain.workbook.repository.WorkbookProblemRepository
import com.solve.domain.workbook.repository.WorkbookQueryRepository
import com.solve.domain.workbook.service.WorkbookSearchService
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkbookSearchServiceImpl(
    private val workbookQueryRepository: WorkbookQueryRepository,
    private val securityHolder: SecurityHolder,
    private val workbookProblemRepository: WorkbookProblemRepository,
    private val workbookLikeRepository: WorkbookLikeRepository,
    private val workbookBookmarkRepository: WorkbookBookmarkRepository
) : WorkbookSearchService {
    @Transactional(readOnly = true)
    override fun searchWorkbook(
        query: String,
        filter: WorkbookSearchFilter?,
        pageable: Pageable
    ): Page<WorkbookResponse> {
        val workbooks = workbookQueryRepository.searchWorkbook(query, filter, pageable)

        return workbooks.map { it.toResponse() }
    }

    private fun Workbook.toResponse() = WorkbookResponse(
        id = id!!,
        title = title,
        description = description,
        problems = workbookProblemRepository.findAllByWorkbook(this).map { WorkbookProblemResponse.of(it) },
        author = WorkbookAuthorResponse.of(author),
        likeCount = workbookLikeRepository.countByWorkbook(this),
        bookmarkCount = workbookBookmarkRepository.countByWorkbook(this),
        createdAt = createdAt,
        updatedAt = updatedAt
    ).apply {
        if (!securityHolder.isAuthenticated) return@apply

        val user = securityHolder.user

        progress = this.problems.intersect(user.solved.map { it.problem }.toSet()).size
        isLiked = workbookLikeRepository.existsByWorkbookAndUser(this@toResponse, user)
        isBookmarked = workbookBookmarkRepository.existsByWorkbookAndUser(this@toResponse, user)
    }
}