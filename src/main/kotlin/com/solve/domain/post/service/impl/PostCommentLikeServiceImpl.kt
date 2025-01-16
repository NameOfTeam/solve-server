package com.solve.domain.post.service.impl

import com.solve.domain.post.domain.entity.PostCommentLike
import com.solve.domain.post.error.PostCommentError
import com.solve.domain.post.error.PostCommentLikeError
import com.solve.domain.post.error.PostError
import com.solve.domain.post.repository.PostCommentLikeRepository
import com.solve.domain.post.repository.PostCommentRepository
import com.solve.domain.post.repository.PostRepository
import com.solve.domain.post.service.PostCommentLikeService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostCommentLikeServiceImpl(
    private val postRepository: PostRepository,
    private val securityHolder: SecurityHolder,
    private val postCommentRepository: PostCommentRepository,
    private val postCommentLikeRepository: PostCommentLikeRepository
) : PostCommentLikeService {
    @Transactional
    override fun likeComment(postId: Long, commentId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val comment = postCommentRepository.findByPostAndId(post, commentId)
            ?: throw CustomException(PostCommentError.POST_COMMENT_NOT_FOUND, commentId)
        val user = securityHolder.user

        if (postCommentLikeRepository.existsByCommentAndUser(comment, user))
            throw CustomException(PostCommentLikeError.POST_COMMENT_ALREADY_LIKED, commentId)

        postCommentLikeRepository.save(
            PostCommentLike(
                comment = comment,
                user = user
            )
        )
    }

    @Transactional
    override fun unlikeComment(postId: Long, commentId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val comment = postCommentRepository.findByPostAndId(post, commentId)
            ?: throw CustomException(PostCommentError.POST_COMMENT_NOT_FOUND, commentId)
        val user = securityHolder.user
        val like = postCommentLikeRepository.findByCommentAndUser(comment, user) ?: throw CustomException(
            PostCommentLikeError.POST_COMMENT_NOT_LIKED,
            commentId
        )

        postCommentLikeRepository.delete(like)
    }
}