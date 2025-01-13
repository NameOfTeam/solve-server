package com.solve.domain.post.service

import com.solve.domain.post.dto.request.PostCommentCreateRequest
import com.solve.domain.post.dto.request.PostCommentUpdateRequest

interface PostCommentService {
    fun createComment(postId: Long, request: PostCommentCreateRequest)
    fun updateComment(postId: Long, commentId: Long, request: PostCommentUpdateRequest)
    fun deleteComment(postId: Long, commentId: Long)
}