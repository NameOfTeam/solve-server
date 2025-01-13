package com.solve.domain.admin.problem.service

import com.solve.domain.admin.problem.dto.request.AdminProblemTestCaseCreateRequest
import com.solve.domain.admin.problem.dto.request.AdminProblemTestCaseUpdateRequest
import com.solve.domain.admin.problem.dto.response.AdminProblemTestCaseResponse

interface AdminProblemTestCaseService {
    fun getProblemTestCases(problemId: Long): List<AdminProblemTestCaseResponse>
    fun addProblemTestCase(problemId: Long, request: AdminProblemTestCaseCreateRequest)
    fun updateProblemTestCase(
        problemId: Long,
        testCaseId: Long,
        request: AdminProblemTestCaseUpdateRequest
    )

    fun removeProblemTestCase(problemId: Long, testCaseId: Long)
}