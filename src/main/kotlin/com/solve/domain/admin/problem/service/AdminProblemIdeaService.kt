package com.solve.domain.admin.problem.service

import com.solve.domain.admin.problem.dto.response.AdminProblemIdeaResponse

interface AdminProblemIdeaService {
    fun getProblemIdeas(problemId: Long): List<AdminProblemIdeaResponse>
}