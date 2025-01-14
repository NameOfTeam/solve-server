package com.solve.domain.post.service

import com.solve.domain.post.dto.request.PostCreateRequest
import com.solve.domain.post.dto.request.PostUpdateRequest
import com.solve.domain.post.dto.response.PostCreateResponse

interface PostService {
    fun createPost(request: PostCreateRequest): PostCreateResponse
    fun updatePost(postId: Long, request: PostUpdateRequest)
    fun deletePost(postId: Long)
}