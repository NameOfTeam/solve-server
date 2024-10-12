package com.solve.domain.problemset.service

import com.solve.domain.problemset.dto.request.ProblemSetProblemAddRequest

interface ProblemSetProblemService {
    fun addProblemSetProblem(problemSetId: Long, request: ProblemSetProblemAddRequest)
    fun removeProblemSetProblem(problemSetId: Long, problemId: Long)
}