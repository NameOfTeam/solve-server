package com.solve.domain.post.repository

import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.entity.PostComment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostCommentRepository: JpaRepository<PostComment, Long> {
    fun findAllByPost(post: Post): List<PostComment>
    fun findAllByPostOrderByCreatedAtDesc(post: Post, pageable: Pageable): Page<PostComment>
    fun findByPostAndId(post: Post, id: Long): PostComment?
}