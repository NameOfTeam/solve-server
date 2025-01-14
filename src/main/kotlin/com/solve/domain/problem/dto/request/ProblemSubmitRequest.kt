package com.solve.domain.problem.dto.request

import com.solve.domain.problem.domain.enums.ProblemSubmitVisibility
import com.solve.global.common.enums.ProgrammingLanguage

data class ProblemSubmitRequest(
    val code: String,
    val language: ProgrammingLanguage,
    val visibility: ProblemSubmitVisibility,
)