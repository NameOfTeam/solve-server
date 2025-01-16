package com.solve.domain.admin.contest.service

import com.solve.domain.admin.contest.dto.request.AdminContestProblemAddRequest

interface AdminContestProblemService {
    fun addContestProblem(contestId: Long, request: AdminContestProblemAddRequest)
    fun removeContestProblem(contestId: Long, problemId: Long)
}