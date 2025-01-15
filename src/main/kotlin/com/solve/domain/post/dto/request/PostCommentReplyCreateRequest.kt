package com.solve.domain.post.dto.request

data class PostCommentReplyCreateRequest(
    val content: String,
    val replyId: Long
)