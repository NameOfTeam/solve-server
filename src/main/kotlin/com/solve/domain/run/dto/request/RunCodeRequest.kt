package com.solve.domain.run.dto.request

import com.solve.global.common.enums.ProgrammingLanguage

data class RunCodeRequest(
    val code: String,
    val language: ProgrammingLanguage
)