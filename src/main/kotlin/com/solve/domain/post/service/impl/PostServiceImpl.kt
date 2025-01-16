package com.solve.domain.post.service.impl

import com.solve.domain.post.domain.entity.Post
import com.solve.domain.post.dto.request.PostCreateRequest
import com.solve.domain.post.dto.request.PostUpdateRequest
import com.solve.domain.post.dto.response.PostAuthorResponse
import com.solve.domain.post.dto.response.PostCreateResponse
import com.solve.domain.post.dto.response.PostProblemResponse
import com.solve.domain.post.dto.response.PostResponse
import com.solve.domain.post.error.PostError
import com.solve.domain.post.repository.PostCommentRepository
import com.solve.domain.post.repository.PostRepository
import com.solve.domain.post.service.PostService
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostServiceImpl(
    private val securityHolder: SecurityHolder,
    private val problemRepository: ProblemRepository,
    private val postRepository: PostRepository,
    private val postCommentRepository: PostCommentRepository
) : PostService {
    @Transactional(readOnly = true)
    override fun getPost(postId: Long): PostResponse {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(
            PostError.POST_NOT_FOUND,
            postId
        )

        return post.toResponse()
    }

    @Transactional
    override fun createPost(request: PostCreateRequest): PostCreateResponse {
        val author = securityHolder.user
        var post = Post(
            title = request.title,
            content = request.content,
            category = request.category,
            author = author,
        )

        request.problemId?.let {
            val problem = problemRepository.findByIdOrNull(it) ?: throw CustomException(
                ProblemError.PROBLEM_NOT_FOUND,
                it
            )

            post.problem = problem
        }

        post = postRepository.save(post)

        return PostCreateResponse.of(post)
    }

    @Transactional
    override fun updatePost(postId: Long, request: PostUpdateRequest) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(
            ProblemError.PROBLEM_NOT_FOUND,
            postId
        )

        val user = securityHolder.user

        if (post.author != user) throw CustomException(
            PostError.POST_NOT_AUTHORIZED,
            postId
        )

        request.title?.let { post.title = it }
        request.content?.let { post.content = it }

        postRepository.save(post)
    }

    @Transactional
    override fun deletePost(postId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(
            PostError.POST_NOT_FOUND,
            postId
        )

        val user = securityHolder.user

        if (post.author != user) throw CustomException(
            PostError.POST_NOT_AUTHORIZED,
            postId
        )

        postRepository.delete(post)
    }

    private fun Post.toResponse() = PostResponse(
        id = id!!,
        title = title,
        content = content,
        category = category,
        language = language,
        likeCount = likes.size.toLong(),
        isLiked = likes.any { it.user == securityHolder.user },
        author = PostAuthorResponse.of(author),
        problem = problem?.let { PostProblemResponse.of(it) },
        commentCount = postCommentRepository.countByPost(this),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}