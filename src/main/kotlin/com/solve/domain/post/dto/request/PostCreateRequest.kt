package com.solve.domain.post.dto.request

import com.solve.domain.post.domain.enums.PostCategory

data class PostCreateRequest(
    val title: String,
    val content: String,
    val problemId: Long,
    val category: PostCategory
)