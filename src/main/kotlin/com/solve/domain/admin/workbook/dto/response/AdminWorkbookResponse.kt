package com.solve.domain.admin.workbook.dto.response

import com.solve.domain.user.domain.entity.User
import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.domain.entity.WorkbookProblem
import java.util.UUID

data class AdminWorkbookResponse(
    val id: Long,
    val title: String,
    val problems: List<AdminWorkbookProblemResponse>,
    val author: AdminWorkbookAuthorResponse,
) {
    companion object {
        fun of(workbook: Workbook) = AdminWorkbookResponse(
            id = workbook.id!!,
            title = workbook.title,
            problems = workbook.problems.map { AdminWorkbookProblemResponse.of(it) },
            author = AdminWorkbookAuthorResponse.of(workbook.author),
        )
    }
}

data class AdminWorkbookProblemResponse(
    val id: Long,
    val title: String,
    val content: String,
    val input: String,
    val output: String,
    val memoryLimit: Long,
    val timeLimit: Double,
) {
    companion object {
        fun of(problem: WorkbookProblem) = AdminWorkbookProblemResponse(
            id = problem.problem.id!!,
            title = problem.problem.title,
            content = problem.problem.content,
            input = problem.problem.input,
            output = problem.problem.output,
            memoryLimit = problem.problem.memoryLimit,
            timeLimit = problem.problem.timeLimit
        )
    }
}

data class AdminWorkbookAuthorResponse(
    val id: UUID,
    val username: String
) {
    companion object {
        fun of(author: User) = AdminWorkbookAuthorResponse(
            id = author.id!!,
            username = author.username
        )
    }
}