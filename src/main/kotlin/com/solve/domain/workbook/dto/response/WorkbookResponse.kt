package com.solve.domain.workbook.dto.response

import com.solve.domain.user.domain.entity.User
import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.domain.entity.WorkbookProblem
import java.time.LocalDateTime

data class WorkbookResponse(
    val id: Long,
    val title: String,
    val problems: List<WorkbookProblemResponse>,
    val author: WorkbookAuthorResponse,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun of(workbook: Workbook) = WorkbookResponse(
            id = workbook.id!!,
            title = workbook.title,
            problems = workbook.problems.map { WorkbookProblemResponse.of(it) },
            author = WorkbookAuthorResponse.of(workbook.author),
            createdAt = workbook.createdAt,
            updatedAt = workbook.updatedAt
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

data class WorkbookAuthorResponse(
    val username: String
) {
    companion object {
        fun of(author: User) = WorkbookAuthorResponse(
            username = author.username
        )
    }
}