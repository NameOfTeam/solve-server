package com.solve.domain.workbook.dto.response

import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.domain.entity.WorkbookProblem

data class WorkbookResponse(
    val title: String,
    val problems: List<WorkbookProblemResponse>
) {
    companion object {
        fun of(workbook: Workbook) = WorkbookResponse(
            title = workbook.title,
            problems = workbook.problems.map { WorkbookProblemResponse.of(it) }
        )
    }
}

data class WorkbookProblemResponse(
    val title: String,
    val content: String,
    val input: String,
    val output: String,
    val memoryLimit: Long,
    val timeLimit: Double,
) {
    companion object {
        fun of(problem: WorkbookProblem) = WorkbookProblemResponse(
            title = problem.problem.title,
            content = problem.problem.content,
            input = problem.problem.input,
            output = problem.problem.output,
            memoryLimit = problem.problem.memoryLimit,
            timeLimit = problem.problem.timeLimit
        )
    }
}