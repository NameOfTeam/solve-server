package com.solve.domain.submit.dto.request

import com.solve.domain.submit.domain.enums.SubmitVisibility
import com.solve.global.common.enums.ProgrammingLanguage

data class SubmitRequest(
    val code: String,
    val language: ProgrammingLanguage,
    val visibility: SubmitVisibility,
)