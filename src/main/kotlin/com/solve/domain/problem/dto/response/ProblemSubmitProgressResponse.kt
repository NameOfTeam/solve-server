package com.solve.domain.problem.dto.response

import com.solve.domain.problem.domain.enums.ProblemSubmitResult

data class ProblemSubmitProgressResponse(
    val submitId: Long,
    val progress: Int,
    val result: ProblemSubmitResult
)