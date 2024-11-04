package com.solve.domain.admin.contest.service

import com.solve.domain.admin.contest.dto.request.AdminContestProblemAddRequest
import com.solve.domain.admin.contest.dto.response.AdminContestResponse

interface AdminContestProblemService {
    fun addContestProblem(contestId: Long, request: AdminContestProblemAddRequest): AdminContestResponse
    fun removeContestProblem(contestId: Long, problemId: Long): AdminContestResponse
}