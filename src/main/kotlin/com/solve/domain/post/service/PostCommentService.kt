package com.solve.domain.post.service

import com.solve.domain.post.dto.request.PostCommentCreateRequest
import com.solve.domain.post.dto.request.PostCommentUpdateRequest
import com.solve.domain.post.dto.response.PostCommentResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostCommentService {
    fun getComments(postId: Long, pageable: Pageable): Page<PostCommentResponse>
    fun createComment(postId: Long, request: PostCommentCreateRequest)
    fun updateComment(postId: Long, commentId: Long, request: PostCommentUpdateRequest)
    fun deleteComment(postId: Long, commentId: Long)
}