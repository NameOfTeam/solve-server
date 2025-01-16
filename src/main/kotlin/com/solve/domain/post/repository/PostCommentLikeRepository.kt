package com.solve.domain.post.repository

import com.solve.domain.post.domain.entity.PostComment
import com.solve.domain.post.domain.entity.PostCommentLike
import com.solve.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface PostCommentLikeRepository : JpaRepository<PostCommentLike, Long> {
    fun findByCommentAndUser(comment: PostComment, user: User): PostCommentLike?

    fun countByComment(comment: PostComment): Long

    fun existsByCommentAndUser(comment: PostComment, user: User): Boolean
}