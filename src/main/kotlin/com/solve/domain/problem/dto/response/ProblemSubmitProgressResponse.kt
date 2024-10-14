package com.solve.domain.problem.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.solve.domain.problem.domain.enums.ProblemSubmitState

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ProblemSubmitProgressResponse(
    val submitId: Long,
    val progress: Double,
    val result: ProblemSubmitState,
    val error: String? = null
)