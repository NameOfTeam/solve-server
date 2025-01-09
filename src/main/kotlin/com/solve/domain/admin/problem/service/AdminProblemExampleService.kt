package com.solve.domain.admin.problem.service

import com.solve.domain.admin.problem.dto.request.AdminProblemExampleCreateRequest
import com.solve.domain.admin.problem.dto.request.AdminProblemExampleUpdateRequest
import com.solve.domain.admin.problem.dto.response.AdminProblemExampleResponse

interface AdminProblemExampleService {
    fun getProblemExamples(problemId: Long): List<AdminProblemExampleResponse>
    fun addProblemExample(problemId: Long, request: AdminProblemExampleCreateRequest)
    fun updateProblemExample(
        problemId: Long,
        exampleId: Long,
        request: AdminProblemExampleUpdateRequest
    )
    fun removeProblemExample(problemId: Long, exampleId: Long)
}