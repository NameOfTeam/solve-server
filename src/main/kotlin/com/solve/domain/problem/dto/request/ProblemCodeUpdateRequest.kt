package com.solve.domain.problem.dto.request

import com.solve.global.common.enums.ProgrammingLanguage

data class ProblemCodeUpdateRequest(
    val code: String?,
    val language: ProgrammingLanguage?
)