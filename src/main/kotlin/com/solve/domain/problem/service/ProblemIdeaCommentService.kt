package com.solve.domain.problem.service

import com.solve.domain.problem.dto.request.ProblemIdeaCommentCreateRequest
import com.solve.domain.problem.dto.request.ProblemIdeaCommentUpdateRequest
import com.solve.domain.problem.dto.response.ProblemIdeaCommentResponse

interface ProblemIdeaCommentService {
    fun getProblemIdeaComments(problemId: Long, ideaId: Long): List<ProblemIdeaCommentResponse>
    fun createProblemIdeaComment(
        problemId: Long,
        ideaId: Long,
        request: ProblemIdeaCommentCreateRequest
    ): ProblemIdeaCommentResponse

    fun updateProblemIdeaComment(
        problemId: Long,
        ideaId: Long,
        commentId: Long,
        request: ProblemIdeaCommentUpdateRequest
    ): ProblemIdeaCommentResponse

    fun deleteProblemIdeaComment(problemId: Long, ideaId: Long, commentId: Long)
}