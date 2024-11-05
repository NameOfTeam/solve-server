package com.solve.domain.problem.dto.request

data class ProblemIdeaCommentCreateRequest(
    val content: String,
    val parentId: Long? = null
)