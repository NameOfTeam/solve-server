package com.solve.domain.post.repository

import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.entity.PostComment
import com.solve.domain.post.domain.entity.PostCommentReply

interface PostCommentReplyQueryRepository {
    fun getReplies(post: Post, comment: PostComment, cursor: PostCommentReply?, size: Int): List<PostCommentReply>
}