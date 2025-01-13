package com.solve.domain.post.service.impl

import com.solve.domain.post.domain.entity.PostComment
import com.solve.domain.post.dto.request.PostCommentCreateRequest
import com.solve.domain.post.dto.request.PostCommentUpdateRequest
import com.solve.domain.post.error.PostCommentError
import com.solve.domain.post.error.PostError
import com.solve.domain.post.repository.PostRepository
import com.solve.domain.post.service.PostCommentService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostCommentServiceImpl(
    private val securityHolder: SecurityHolder,
    private val postRepository: PostRepository
): PostCommentService {
    @Transactional
    override fun createComment(postId: Long, request: PostCommentCreateRequest) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val author = securityHolder.user
        val comment = PostComment(
            post = post,
            content = request.content,
            author = author
        )

        post.comments.add(comment)
    }

    @Transactional
    override fun updateComment(postId: Long, commentId: Long, request: PostCommentUpdateRequest) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val comment = post.comments.find { it.id == commentId } ?: throw CustomException(PostCommentError.POST_COMMENT_NOT_FOUND, commentId)

        if (comment.author != securityHolder.user)
            throw CustomException(PostCommentError.POST_COMMENT_NOT_AUTHORIZED)

        request.content?.let { comment.content = it }
    }

    @Transactional
    override fun deleteComment(postId: Long, commentId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val comment = post.comments.find { it.id == commentId } ?: throw CustomException(PostCommentError.POST_COMMENT_NOT_FOUND, commentId)

        if (comment.author != securityHolder.user)
            throw CustomException(PostCommentError.POST_COMMENT_NOT_AUTHORIZED)

        post.comments.remove(comment)
    }
}