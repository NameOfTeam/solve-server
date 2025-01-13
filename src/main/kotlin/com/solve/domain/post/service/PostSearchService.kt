package com.solve.domain.post.service

import com.solve.domain.post.domain.enums.PostCategory
import com.solve.domain.post.dto.response.PostResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostSearchService {
    fun searchPost(query: String, category: PostCategory?, pageable: Pageable): Page<PostResponse>
}