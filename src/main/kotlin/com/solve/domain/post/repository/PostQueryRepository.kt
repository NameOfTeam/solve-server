package com.solve.domain.post.repository

import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.enums.PostCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostQueryRepository {
    fun searchPost(query: String, category: PostCategory?, pageable: Pageable): Page<Post>
}