package com.solve.domain.post.repository

import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.entity.PostComment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostCommentQueryRepository {
    fun getComments(post: Post, pageable: Pageable): Page<PostComment>
}