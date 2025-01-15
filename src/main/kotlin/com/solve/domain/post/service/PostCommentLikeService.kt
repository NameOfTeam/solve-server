package com.solve.domain.post.service

interface PostCommentLikeService {
    fun likeComment(postId: Long, commentId: Long)
    fun unlikeComment(postId: Long, commentId: Long)
}