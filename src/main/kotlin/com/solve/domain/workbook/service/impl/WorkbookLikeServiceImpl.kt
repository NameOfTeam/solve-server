package com.solve.domain.workbook.service.impl

import com.solve.domain.workbook.domain.entity.WorkbookLike
import com.solve.domain.workbook.error.WorkbookError
import com.solve.domain.workbook.error.WorkbookLikeError
import com.solve.domain.workbook.repository.WorkbookLikeRepository
import com.solve.domain.workbook.repository.WorkbookRepository
import com.solve.domain.workbook.service.WorkbookLikeService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkbookLikeServiceImpl(
    private val workbookRepository: WorkbookRepository,
    private val securityHolder: SecurityHolder,
    private val workbookLikeRepository: WorkbookLikeRepository
) : WorkbookLikeService {
    @Transactional
    override fun addWorkbookLike(workbookId: Long) {
        val workbook =
            workbookRepository.findByIdOrNull(workbookId) ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)
        val user = securityHolder.user

        if (workbookLikeRepository.existsByWorkbookAndUser(workbook, user))
            throw CustomException(WorkbookLikeError.WORKBOOK_LIKE_ALREADY_EXISTS)

        workbookLikeRepository.save(WorkbookLike(workbook = workbook, user = user))
    }

    @Transactional
    override fun removeWorkbookLike(workbookId: Long) {
        val workbook =
            workbookRepository.findByIdOrNull(workbookId) ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)
        val user = securityHolder.user
        val workbookLike =
            workbookLikeRepository.findByWorkbookAndUser(workbook, user)
                ?: throw CustomException(WorkbookLikeError.WORKBOOK_LIKE_NOT_FOUND)

        workbookLikeRepository.delete(workbookLike)
    }
}