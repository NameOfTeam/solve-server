package com.solve.domain.problem.dto.request

import com.solve.domain.problem.domain.enums.ProblemSubmitLanguage
import com.solve.domain.problem.domain.enums.ProblemSubmitVisibility

data class ProblemSubmitRequest(
    val code: String,
    val language: ProblemSubmitLanguage,
    val visibility: ProblemSubmitVisibility,
)