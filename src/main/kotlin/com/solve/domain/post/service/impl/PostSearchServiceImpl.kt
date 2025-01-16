package com.solve.domain.post.service.impl

import com.solve.domain.post.domain.enums.PostCategory
import com.solve.domain.post.dto.response.PostResponse
import com.solve.domain.post.mapper.PostMapper
import com.solve.domain.post.repository.PostQueryRepository
import com.solve.domain.post.service.PostSearchService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostSearchServiceImpl(
    private val postQueryRepository: PostQueryRepository,
    private val postMapper: PostMapper
) : PostSearchService {
    @Transactional(readOnly = true)
    override fun searchPost(query: String, category: PostCategory?, pageable: Pageable): Page<PostResponse> {
        val posts = postQueryRepository.searchPost(query, category, pageable)

        return posts.map { postMapper.toResponse(it) }
    }
}