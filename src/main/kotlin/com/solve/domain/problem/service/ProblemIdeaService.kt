package com.solve.domain.problem.service

import com.solve.domain.problem.dto.request.ProblemIdeaCreateRequest
import com.solve.domain.problem.dto.request.ProblemIdeaUpdateRequest
import com.solve.domain.problem.dto.response.ProblemIdeaResponse

interface ProblemIdeaService {
    fun getProblemIdeas(problemId: Long): List<ProblemIdeaResponse>
    fun getProblemIdea(problemId: Long, ideaId: Long): ProblemIdeaResponse
    fun createProblemIdea(problemId: Long, request: ProblemIdeaCreateRequest): ProblemIdeaResponse
    fun updateProblemIdea(problemId: Long, ideaId: Long, request: ProblemIdeaUpdateRequest): ProblemIdeaResponse
    fun deleteProblemIdea(problemId: Long, ideaId: Long): ProblemIdeaResponse
}