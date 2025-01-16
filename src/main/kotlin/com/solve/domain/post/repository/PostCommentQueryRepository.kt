package com.solve.domain.post.repository

import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.entity.PostComment

interface PostCommentQueryRepository {
    fun getComments(post: Post, cursor: PostComment?, size: Int): List<PostComment>
}