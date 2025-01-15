package com.solve.domain.post.service

import com.solve.domain.post.dto.request.PostCommentReplyCreateRequest
import com.solve.domain.post.dto.request.PostCommentReplyUpdateRequest
import com.solve.domain.post.dto.response.PostCommentReplyResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostCommentReplyService {
    fun getReplies(postId: Long, commentId: Long, pageable: Pageable): Page<PostCommentReplyResponse>
    fun createReply(postId: Long, commentId: Long, request: PostCommentReplyCreateRequest)
    fun updateReply(postId: Long, commentId: Long, replyId: Long, request: PostCommentReplyUpdateRequest)
    fun deleteReply(postId: Long, commentId: Long, replyId: Long)
}