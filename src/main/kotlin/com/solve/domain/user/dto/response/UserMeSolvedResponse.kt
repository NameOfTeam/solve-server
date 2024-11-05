package com.solve.domain.user.dto.response

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.user.domain.entity.UserSolved
import java.time.LocalDate

data class UserMeSolvedResponse(
    val problem: UserMeSolvedProblemResponse,
    val solvedAt: LocalDate
) {
    companion object {
        fun of(solved: UserSolved) = UserMeSolvedResponse(
            problem = UserMeSolvedProblemResponse.of(solved.problem),
            solvedAt = solved.date
        )
    }
}

data class UserMeSolvedProblemResponse(
    val title: String,
    val content: String,
    val input: String,
    val output: String,
    val memoryLimit: Long,
    val timeLimit: Double,
) {
    companion object {
        fun of(problem: Problem) = UserMeSolvedProblemResponse(
            title = problem.title,
            content = problem.content,
            input = problem.input,
            output = problem.output,
            memoryLimit = problem.memoryLimit,
            timeLimit = problem.timeLimit
        )
    }
}