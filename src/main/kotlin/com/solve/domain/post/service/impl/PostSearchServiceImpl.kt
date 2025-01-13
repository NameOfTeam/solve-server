package com.solve.domain.post.service.impl

import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.domain.enums.PostCategory
import com.solve.domain.post.dto.response.PostAuthorResponse
import com.solve.domain.post.dto.response.PostProblemResponse
import com.solve.domain.post.dto.response.PostResponse
import com.solve.domain.post.repository.PostQueryRepository
import com.solve.domain.post.service.PostSearchService
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostSearchServiceImpl(
    private val securityHolder: SecurityHolder,
    private val postQueryRepository: PostQueryRepository
): PostSearchService {
    @Transactional(readOnly = true)
    override fun searchPost(query: String, category: PostCategory?, pageable: Pageable): Page<PostResponse> {
        val posts = postQueryRepository.searchPost(query, category, pageable)

        return posts.map { it.toResponse() }
    }

    private fun Post.toResponse() = PostResponse(
        id = id!!,
        title = title,
        content = content,
        category = category,
        author = PostAuthorResponse.of(author),
        problem = problem?.let { PostProblemResponse.of(it) },
        language = language,
        likeCount = likes.size.toLong(),
        isLiked = securityHolder.isAuthenticated && likes.any { it.user == securityHolder.user },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}