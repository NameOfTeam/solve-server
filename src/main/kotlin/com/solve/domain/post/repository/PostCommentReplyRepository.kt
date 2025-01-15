package com.solve.domain.post.repository

import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.entity.PostComment
import com.solve.domain.post.domain.entity.PostCommentReply
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface PostCommentReplyRepository: JpaRepository<PostCommentReply, Long> {
    fun findAllByPostAndComment(post: Post, comment: PostComment): List<PostCommentReply>
    fun findAllByPostAndCommentOrderByCreatedAt(post: Post, comment: PostComment, pageable: Pageable): Page<PostCommentReply>
    fun findByPostAndCommentAndId(post: Post, comment: PostComment, id: Long): PostCommentReply?

    fun countByComment(comment: PostComment): Long
}