package com.solve.domain.workbook.repository

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.domain.entity.WorkbookProblem
import org.springframework.data.jpa.repository.JpaRepository

interface WorkbookProblemRepository : JpaRepository<WorkbookProblem, Long> {
    fun findAllByWorkbook(workbook: Workbook): List<WorkbookProblem>
    fun findByWorkbookAndProblem(workbook: Workbook, problem: Problem): WorkbookProblem?
}