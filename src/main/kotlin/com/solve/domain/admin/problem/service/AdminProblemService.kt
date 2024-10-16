package com.solve.domain.admin.problem.service

import com.solve.domain.admin.problem.dto.request.AdminProblemCreateRequest
import com.solve.domain.admin.problem.dto.response.AdminProblemResponse
import com.solve.domain.problem.dto.response.ProblemResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface AdminProblemService {
    fun getProblems(pageable: Pageable): Slice<AdminProblemResponse>
    fun getProblem(problemId: Long): AdminProblemResponse
    fun createProblem(request: AdminProblemCreateRequest): ProblemResponse

}