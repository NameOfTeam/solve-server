package com.solve.domain.post.dto.response

import com.solve.domain.post.domain.entity.Post

data class PostCreateResponse(
    val id: Long
) {
    companion object {
        fun of(post: Post) = PostCreateResponse(
            id = post.id!!
        )
    }
}