package com.solve.domain.post.service.impl

import com.solve.domain.post.domain.entity.PostCommentReplyLike
import com.solve.domain.post.error.PostCommentError
import com.solve.domain.post.error.PostCommentReplyError
import com.solve.domain.post.error.PostCommentReplyLikeError
import com.solve.domain.post.error.PostError
import com.solve.domain.post.repository.PostCommentReplyLikeRepository
import com.solve.domain.post.repository.PostCommentReplyRepository
import com.solve.domain.post.repository.PostCommentRepository
import com.solve.domain.post.repository.PostRepository
import com.solve.domain.post.service.PostCommentReplyLikeService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostCommentReplyLikeServiceImpl(
    private val postRepository: PostRepository,
    private val securityHolder: SecurityHolder,
    private val postCommentRepository: PostCommentRepository,
    private val postCommentReplyRepository: PostCommentReplyRepository,
    private val postCommentReplyLikeRepository: PostCommentReplyLikeRepository
) : PostCommentReplyLikeService {
    @Transactional
    override fun likeCommentReply(postId: Long, commentId: Long, replyId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val comment = postCommentRepository.findByPostAndId(post, commentId)
            ?: throw CustomException(PostCommentError.POST_COMMENT_NOT_FOUND, commentId)
        val reply =
            postCommentReplyRepository.findByPostAndCommentAndId(post, comment, replyId) ?: throw CustomException(
                PostCommentReplyError.POST_COMMENT_REPLY_NOT_FOUND,
                replyId
            )
        val user = securityHolder.user

        if (postCommentReplyLikeRepository.existsByReplyAndUser(reply, user))
            throw CustomException(PostCommentReplyLikeError.POST_COMMENT_REPLY_ALREADY_LIKED, replyId)

        postCommentReplyLikeRepository.save(
            PostCommentReplyLike(
                reply = reply,
                user = user
            )
        )
    }

    @Transactional
    override fun unlikeCommentReply(postId: Long, commentId: Long, replyId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val comment = postCommentRepository.findByPostAndId(post, commentId)
            ?: throw CustomException(PostCommentError.POST_COMMENT_NOT_FOUND, commentId)
        val reply =
            postCommentReplyRepository.findByPostAndCommentAndId(post, comment, replyId) ?: throw CustomException(
                PostCommentReplyError.POST_COMMENT_REPLY_NOT_FOUND,
                replyId
            )
        val user = securityHolder.user
        val like = postCommentReplyLikeRepository.findByReplyAndUser(reply, user) ?: throw CustomException(
            PostCommentReplyLikeError.POST_COMMENT_REPLY_NOT_LIKED,
            replyId
        )

        postCommentReplyLikeRepository.delete(like)
    }
}