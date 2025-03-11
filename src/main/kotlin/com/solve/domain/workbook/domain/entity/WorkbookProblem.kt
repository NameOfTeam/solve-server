package com.solve.domain.workbook.domain.entity

import com.solve.domain.problem.domain.entity.Problem
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(
    name = "workbook_problems",
    indexes = [
        Index(name = "idx_workbook_problem_workbook", columnList = "workbook_id"),
        Index(name = "idx_workbook_problem_problem", columnList = "problem_id")
    ]
)
class WorkbookProblem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id", nullable = false)
    val workbook: Workbook,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WorkbookProblem) return false

        if (workbook.id != other.workbook.id) return false
        if (problem.id != other.problem.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = workbook.id?.hashCode() ?: 0
        result = 31 * result + (problem.id?.hashCode() ?: 0)
        return result
    }
}