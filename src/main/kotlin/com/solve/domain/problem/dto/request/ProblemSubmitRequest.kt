package com.solve.domain.problem.dto.request

import com.solve.global.common.enums.ProgrammingLanguage
import com.solve.domain.problem.domain.enums.ProblemSubmitVisibility

data class ProblemSubmitRequest(
    val code: String,
    val language: ProgrammingLanguage,
    val visibility: ProblemSubmitVisibility,
)