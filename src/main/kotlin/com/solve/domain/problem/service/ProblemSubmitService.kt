package com.solve.domain.problem.service

import com.solve.domain.problem.dto.request.ProblemSubmitRequest
import com.solve.domain.problem.dto.response.ProblemSubmitResponse

interface ProblemSubmitService {
    fun submitProblem(problemId: Long, request: ProblemSubmitRequest): ProblemSubmitResponse
}