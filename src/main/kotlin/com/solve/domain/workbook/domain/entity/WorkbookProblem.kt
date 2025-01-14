package com.solve.domain.workbook.domain.entity

import com.solve.domain.problem.domain.entity.Problem
import jakarta.persistence.*

@Entity
@Table(name = "workbook_problems")
class WorkbookProblem(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id", nullable = false)
    val workbook: Workbook,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,
)