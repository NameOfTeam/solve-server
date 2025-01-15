package com.solve.domain.workbook.repository

import com.solve.domain.user.domain.entity.User
import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.domain.entity.WorkbookLike
import org.springframework.data.jpa.repository.JpaRepository

interface WorkbookLikeRepository: JpaRepository<WorkbookLike, Long> {
    fun findByWorkbookAndUser(workbook: Workbook, user: User): WorkbookLike?

    fun countByWorkbook(workbook: Workbook): Long

    fun existsByWorkbookAndUser(workbook: Workbook, user: User): Boolean
}