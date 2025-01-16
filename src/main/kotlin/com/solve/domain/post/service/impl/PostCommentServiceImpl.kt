package com.solve.domain.post.service.impl

import com.solve.domain.post.domain.entity.PostComment
import com.solve.domain.post.dto.request.PostCommentCreateRequest
import com.solve.domain.post.dto.request.PostCommentUpdateRequest
import com.solve.domain.post.dto.response.PostCommentAuthorResponse
import com.solve.domain.post.dto.response.PostCommentResponse
import com.solve.domain.post.error.PostCommentError
import com.solve.domain.post.error.PostError
import com.solve.domain.post.repository.*
import com.solve.domain.post.service.PostCommentService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostCommentServiceImpl(
    private val securityHolder: SecurityHolder,
    private val postRepository: PostRepository,
    private val postCommentRepository: PostCommentRepository,
    private val postCommentQueryRepository: PostCommentQueryRepository,
    private val postCommentLikeRepository: PostCommentLikeRepository,
    private val postCommentReplyRepository: PostCommentReplyRepository,
) : PostCommentService {
    @Transactional(readOnly = true)
    override fun getComments(postId: Long, cursorId: Long?, size: Int): List<PostCommentResponse> {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val cursor = cursorId?.let {
            postCommentRepository.findByIdOrNull(it)
                ?: throw CustomException(PostCommentError.POST_COMMENT_CURSOR_NOT_FOUND, it)
        }
        val comments = postCommentQueryRepository.getComments(post, cursor, size)

        return comments.map { it.toResponse() }
    }

    @Transactional
    override fun createComment(postId: Long, request: PostCommentCreateRequest) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val author = securityHolder.user

        val comment = PostComment(
            post = post,
            content = request.content,
            author = author
        )

        postCommentRepository.save(comment)
    }

    @Transactional
    override fun updateComment(postId: Long, commentId: Long, request: PostCommentUpdateRequest) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val comment = postCommentRepository.findByPostAndId(post, commentId) ?: throw CustomException(
            PostCommentError.POST_COMMENT_NOT_FOUND,
            commentId
        )

        if (comment.author != securityHolder.user)
            throw CustomException(PostCommentError.POST_COMMENT_NOT_AUTHORIZED)

        request.content?.let { comment.content = it }
    }

    @Transactional
    override fun deleteComment(postId: Long, commentId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val comment = postCommentRepository.findByPostAndId(post, commentId) ?: throw CustomException(
            PostCommentError.POST_COMMENT_NOT_FOUND,
            commentId
        )

        if (comment.author != securityHolder.user)
            throw CustomException(PostCommentError.POST_COMMENT_NOT_AUTHORIZED)

        postCommentRepository.delete(comment)
    }

    private fun PostComment.toResponse() = PostCommentResponse(
        id = id!!,
        content = content,
        author = PostCommentAuthorResponse.of(author),
        likeCount = postCommentLikeRepository.countByComment(this),
        isLiked = securityHolder.isAuthenticated && postCommentLikeRepository.existsByCommentAndUser(
            this,
            securityHolder.user
        ),
        replyCount = postCommentReplyRepository.countByComment(this),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}