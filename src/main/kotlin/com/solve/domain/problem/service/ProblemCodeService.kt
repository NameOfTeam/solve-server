package com.solve.domain.problem.service

import com.solve.domain.problem.dto.request.ProblemCodeCreateRequest
import com.solve.domain.problem.dto.request.ProblemCodeUpdateRequest
import com.solve.domain.problem.dto.response.ProblemCodeResponse

interface ProblemCodeService {
    fun saveCode(problemId: Long, request: ProblemCodeCreateRequest) : ProblemCodeResponse
    fun updateCode(problemCodeId: Long, request: ProblemCodeUpdateRequest) : ProblemCodeResponse
    fun deleteCode(problemCodeId: Long) : ProblemCodeResponse
    fun getCode(problemCodeId: Long) : ProblemCodeResponse
}