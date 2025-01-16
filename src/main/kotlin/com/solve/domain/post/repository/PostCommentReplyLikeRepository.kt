package com.solve.domain.post.repository

import com.solve.domain.post.domain.entity.PostCommentReply
import com.solve.domain.post.domain.entity.PostCommentReplyLike
import com.solve.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface PostCommentReplyLikeRepository : JpaRepository<PostCommentReplyLike, Long> {
    fun findByReplyAndUser(reply: PostCommentReply, user: User): PostCommentReplyLike?

    fun countByReply(reply: PostCommentReply): Long

    fun existsByReplyAndUser(reply: PostCommentReply, user: User): Boolean
}