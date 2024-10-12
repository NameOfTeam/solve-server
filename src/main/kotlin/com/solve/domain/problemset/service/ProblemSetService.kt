package com.solve.domain.problemset.service

import com.solve.domain.problemset.dto.request.ProblemSetCreateRequest
import com.solve.domain.problemset.dto.request.ProblemSetUpdateRequest
import com.solve.domain.problemset.dto.response.ProblemSetResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProblemSetService {
    fun getProblemSets(pageable: Pageable): Page<ProblemSetResponse>
    fun getProblemSet(problemSetId: Long): ProblemSetResponse
    fun createProblemSet(request: ProblemSetCreateRequest): ProblemSetResponse
    fun updateProblemSet(problemSetId: Long, request: ProblemSetUpdateRequest): ProblemSetResponse
    fun deleteProblemSet(problemSetId: Long)
}