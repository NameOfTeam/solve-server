package com.solve.domain.post.service

interface PostCommentReplyLikeService {
    fun likeCommentReply(postId: Long, commentId: Long, replyId: Long)
    fun unlikeCommentReply(postId: Long, commentId: Long, replyId: Long)
}