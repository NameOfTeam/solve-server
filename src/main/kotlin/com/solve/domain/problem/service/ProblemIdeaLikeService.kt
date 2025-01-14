package com.solve.domain.problem.service

interface ProblemIdeaLikeService {
    fun toggleProblemIdeaLike(problemId: Long, ideaId: Long)
}