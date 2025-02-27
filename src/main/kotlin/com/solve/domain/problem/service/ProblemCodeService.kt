package com.solve.domain.problem.service

import com.solve.domain.problem.dto.request.ProblemCodeCreateRequest
import com.solve.domain.problem.dto.response.ProblemCodeResponse
import com.solve.global.common.enums.ProgrammingLanguage

interface ProblemCodeService {
    fun saveCode(problemId: Long, language: ProgrammingLanguage, request: ProblemCodeCreateRequest)
    fun getCode(problemId: Long, language: ProgrammingLanguage): ProblemCodeResponse
    fun deleteCode(problemId: Long, language: ProgrammingLanguage)
}