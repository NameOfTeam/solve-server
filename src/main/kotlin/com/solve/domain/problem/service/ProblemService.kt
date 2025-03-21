package com.solve.domain.problem.service

import com.solve.domain.problem.dto.response.ProblemResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProblemService {
    fun getProblems(pageable: Pageable): Page<ProblemResponse>
    fun getProblem(problemId: Long): ProblemResponse
}