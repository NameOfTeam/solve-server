package com.solve.domain.admin.problem.service

import com.solve.domain.problem.dto.request.ProblemTestCaseCreateRequest
import com.solve.domain.problem.dto.request.ProblemTestCaseUpdateRequest
import com.solve.domain.problem.dto.response.ProblemTestCaseResponse

interface AdminProblemTestCaseService {
    fun getProblemTestCases(problemId: Long): List<ProblemTestCaseResponse>
    fun addProblemTestCase(problemId: Long, request: ProblemTestCaseCreateRequest)
    fun updateProblemTestCase(
        problemId: Long,
        testCaseId: Long,
        request: ProblemTestCaseUpdateRequest
    ): ProblemTestCaseResponse

    fun removeProblemTestCase(problemId: Long, testCaseId: Long)
}