package com.solve.domain.problem.dto.response

import com.solve.domain.problem.domain.entity.ProblemCode
import com.solve.global.common.enums.ProgrammingLanguage

data class ProblemCodeResponse(
    val code: String,
    val language: ProgrammingLanguage
) {
    companion object {
        fun of(problemCode: ProblemCode): ProblemCodeResponse {
            return ProblemCodeResponse(
                code = problemCode.code,
                language = problemCode.language
            )
        }
    }
}