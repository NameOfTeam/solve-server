package com.solve.domain.submit.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.solve.domain.submit.domain.enums.SubmitState
import com.solve.global.common.enums.ProgrammingLanguage

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SubmitProgressResponse(
    val submitId: Long,
    val progress: Double,
    val result: SubmitState,
    val error: String? = null,
    val timeUsage: Long? = null,
    val memoryUsage: Long? = null,
    val language: ProgrammingLanguage? = null
)