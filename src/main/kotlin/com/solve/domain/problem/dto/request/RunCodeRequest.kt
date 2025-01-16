package com.solve.domain.problem.dto.request

import com.solve.global.common.enums.ProgrammingLanguage

data class RunCodeRequest(
    val problemId: Long,
    val code: String,
    val language: ProgrammingLanguage
)