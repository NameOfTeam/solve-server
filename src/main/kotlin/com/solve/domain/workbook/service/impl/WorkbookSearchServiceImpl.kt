package com.solve.domain.workbook.service.impl

import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.domain.enums.WorkbookSearchFilter
import com.solve.domain.workbook.dto.response.WorkbookAuthorResponse
import com.solve.domain.workbook.dto.response.WorkbookProblemResponse
import com.solve.domain.workbook.dto.response.WorkbookResponse
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
    private val securityHolder: SecurityHolder
): WorkbookSearchService {
    @Transactional(readOnly = true)
    override fun searchWorkbook(query: String, filter: WorkbookSearchFilter?, pageable: Pageable): Page<WorkbookResponse> {
        val workbooks = workbookQueryRepository.searchWorkbook(query, filter, pageable)

        return workbooks.map { it.toResponse() }
    }


    private fun Workbook.toResponse() = WorkbookResponse(
        id = id!!,
        title = title,
        description = description,
        problems = problems.map { WorkbookProblemResponse.of(it) },
        author = WorkbookAuthorResponse.of(author),
        likeCount = likes.size.toLong(),
        bookmarkCount = bookmarks.size.toLong(),
        createdAt = createdAt,
        updatedAt = updatedAt
    ).apply {
        if (securityHolder.isAuthenticated) {
            val user = securityHolder.user

            progress = this.problems.intersect(user.solved.map { it.problem }.toSet()).size
            liked = likes.any { it.user == user }
            bookmarked = bookmarks.any { it.user == user }
        }
    }
}