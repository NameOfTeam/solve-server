package com.solve.domain.workbook.service.impl

import com.solve.domain.workbook.domain.entity.WorkbookBookmark
import com.solve.domain.workbook.error.WorkbookBookmarkError
import com.solve.domain.workbook.error.WorkbookError
import com.solve.domain.workbook.repository.WorkbookBookmarkRepository
import com.solve.domain.workbook.repository.WorkbookRepository
import com.solve.domain.workbook.service.WorkbookBookmarkService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkbookBookmarkServiceImpl(
    private val workbookRepository: WorkbookRepository,
    private val securityHolder: SecurityHolder,
    private val workbookBookmarkRepository: WorkbookBookmarkRepository
) : WorkbookBookmarkService {
    @Transactional
    override fun addWorkbookBookmark(workbookId: Long) {
        val workbook = workbookRepository
            .findByIdOrNull(workbookId) ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)
        val user = securityHolder.user

        if (workbookBookmarkRepository.existsByWorkbookAndUser(workbook, user))
            throw CustomException(WorkbookBookmarkError.WORKBOOK_BOOKMARK_ALREADY_EXISTS)

        workbookBookmarkRepository.save(WorkbookBookmark(workbook = workbook, user = user))
    }

    @Transactional
    override fun removeWorkbookBookmark(workbookId: Long) {
        val workbook =
            workbookRepository.findByIdOrNull(workbookId) ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)
        val user = securityHolder.user
        val workbookBookmark = workbookBookmarkRepository.findByWorkbookAndUser(workbook, user)
            ?: throw CustomException(WorkbookBookmarkError.WORKBOOK_BOOKMARK_NOT_FOUND)

        workbookBookmarkRepository.delete(workbookBookmark)
    }
}