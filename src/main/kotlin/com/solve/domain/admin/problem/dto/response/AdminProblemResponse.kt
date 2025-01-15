package com.solve.domain.admin.problem.dto.response

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemTestCase
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.user.domain.entity.User

data class AdminProblemResponse(
    val id: Long,
    val title: String,
    val content: String,
    val input: String,
    val output: String,
    val memoryLimit: Long,
    val timeLimit: Double,
    var correctRate: Double,
    val author: AdminProblemAuthorResponse,
    val contributors: List<AdminProblemContributorResponse>,
    val testCases: List<AdminProblemTestCaseResponse>
)

data class AdminProblemAuthorResponse(
    val username: String
) {
    companion object {
        fun of(user: User) = AdminProblemAuthorResponse(
            username = user.username
        )
    }
}

data class AdminProblemContributorResponse(
    val username: String
) {
    companion object {
        fun of(user: User) = AdminProblemContributorResponse(
            username = user.username
        )
    }
}

data class AdminProblemTestCaseResponse(
    val input: String,
    val output: String,
) {
    companion object {
        fun of(testCase: ProblemTestCase) = AdminProblemTestCaseResponse(
            input = testCase.input,
            output = testCase.output,
        )
    }
}