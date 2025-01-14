package com.solve.domain.post.service

import com.solve.domain.post.dto.request.PostCreateRequest
import com.solve.domain.post.dto.request.PostUpdateRequest

interface PostService {
    fun createPost(request: PostCreateRequest)
    fun updatePost(postId: Long, request: PostUpdateRequest)
    fun deletePost(postId: Long)
}