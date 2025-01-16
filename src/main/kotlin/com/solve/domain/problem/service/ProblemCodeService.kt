package com.solve.domain.problem.service

import com.solve.domain.problem.dto.request.ProblemCodeCreateRequest
import com.solve.domain.problem.dto.response.ProblemCodeResponse

interface ProblemCodeService {
    fun saveCode(problemId: Long, request: ProblemCodeCreateRequest)
    fun getCode(problemId: Long): ProblemCodeResponse
    fun deleteCode(problemId: Long)
}