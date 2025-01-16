package com.solve.domain.post.repository

import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.entity.PostComment
import org.springframework.data.jpa.repository.JpaRepository

interface PostCommentRepository: JpaRepository<PostComment, Long> {
    fun findByPostAndId(post: Post, id: Long): PostComment?

    fun countByPost(post: Post): Long
}