package com.solve.domain.workbook.service.impl

import com.solve.domain.workbook.error.WorkbookError
import com.solve.domain.workbook.repository.WorkbookRepository
import com.solve.domain.workbook.service.WorkbookLikeService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WorkbookLikeServiceImpl(private val workbookRepository: WorkbookRepository, private val securityHolder: SecurityHolder) : WorkbookLikeService {
    @Transactional
    override fun addWorkbookLike(workbookId: Long) {
        val workbook = workbookRepository.findByIdOrNull(workbookId) ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)
        val user = securityHolder.user

        workbook.addLike(user)
    }

    @Transactional
    override fun removeWorkbookLike(workbookId: Long) {
        val workbook = workbookRepository.findByIdOrNull(workbookId) ?: throw CustomException(WorkbookError.WORKBOOK_NOT_FOUND)
        val user = securityHolder.user

        workbook.removeLike(user)
    }
}