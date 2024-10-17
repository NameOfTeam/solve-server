package com.solve.domain.contest.service

import com.solve.domain.contest.dto.request.ContestProblemAddRequest

interface ContestProblemService {
    fun addContestProblem(contestId: Long, request: ContestProblemAddRequest)
    fun removeContestProblem(contestId: Long, problemId: Long)
}