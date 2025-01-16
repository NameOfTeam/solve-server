package com.solve.domain.workbook.repository

import com.solve.domain.user.domain.entity.User
import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.domain.entity.WorkbookBookmark
import org.springframework.data.jpa.repository.JpaRepository

interface WorkbookBookmarkRepository : JpaRepository<WorkbookBookmark, Long> {
    fun findByWorkbookAndUser(workbook: Workbook, user: User): WorkbookBookmark?

    fun existsByWorkbookAndUser(workbook: Workbook, user: User): Boolean

    fun countByWorkbook(workbook: Workbook): Long
}