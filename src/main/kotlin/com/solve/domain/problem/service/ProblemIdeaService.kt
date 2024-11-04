package com.solve.domain.problem.service

import com.solve.domain.problem.dto.request.ProblemIdeaCreateRequest
import com.solve.domain.problem.dto.request.ProblemIdeaUpdateRequest
import com.solve.domain.problem.dto.response.ProblemIdeaResponse

interface ProblemIdeaService {
    fun getMyProblemIdeas(problemId: Long): List<ProblemIdeaResponse>
    fun createProblemIdea(problemId: Long, request: ProblemIdeaCreateRequest): ProblemIdeaResponse
    fun updateProblemIdea(problemId: Long, ideaId: Long, request: ProblemIdeaUpdateRequest): ProblemIdeaResponse
    fun deleteProblemIdea(problemId: Long, ideaId: Long): ProblemIdeaResponse
}