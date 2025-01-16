package com.solve.domain.post.service

import com.solve.domain.post.dto.request.PostCommentReplyCreateRequest
import com.solve.domain.post.dto.request.PostCommentReplyUpdateRequest
import com.solve.domain.post.dto.response.PostCommentReplyResponse

interface PostCommentReplyService {
    fun getReplies(postId: Long, commentId: Long, cursorId: Long?, size: Int): List<PostCommentReplyResponse>
    fun createReply(postId: Long, commentId: Long, request: PostCommentReplyCreateRequest)
    fun updateReply(postId: Long, commentId: Long, replyId: Long, request: PostCommentReplyUpdateRequest)
    fun deleteReply(postId: Long, commentId: Long, replyId: Long)
}