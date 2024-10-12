package com.solve.domain.problem.dto.response

import com.solve.domain.problem.domain.enums.ProblemSubmitState

data class ProblemSubmitProgressResponse(
    val submitId: Long,
    val progress: Double,
    val result: ProblemSubmitState
)