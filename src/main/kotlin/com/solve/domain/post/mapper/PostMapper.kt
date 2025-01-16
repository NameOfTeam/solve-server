package com.solve.domain.post.mapper

import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.dto.response.PostAuthorResponse
import com.solve.domain.post.dto.response.PostProblemResponse
import com.solve.domain.post.dto.response.PostResponse
import com.solve.domain.post.repository.PostCommentRepository
import com.solve.domain.post.repository.PostLikeRepository
import com.solve.global.security.holder.SecurityHolder
import org.springframework.stereotype.Component

@Component
class PostMapper(
    private val securityHolder: SecurityHolder,
    private val postCommentRepository: PostCommentRepository,
    private val postLikeRepository: PostLikeRepository
) {
    fun toResponse(post: Post) = PostResponse(
        id = post.id!!,
        title = post.title,
        content = post.content,
        category = post.category,
        language = post.language,
        likeCount = postLikeRepository.countByPost(post),
        isLiked = postLikeRepository.existsByPostAndUser(post, securityHolder.user),
        author = PostAuthorResponse.of(post.author),
        problem = post.problem?.let { PostProblemResponse.of(it) },
        commentCount = postCommentRepository.countByPost(post),
        createdAt = post.createdAt,
        updatedAt = post.updatedAt
    )
}