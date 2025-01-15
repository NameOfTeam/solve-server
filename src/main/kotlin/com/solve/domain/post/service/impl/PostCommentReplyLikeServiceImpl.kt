package com.solve.domain.post.service.impl

import com.solve.domain.post.domain.entity.PostCommentReplyLike
import com.solve.domain.post.error.PostCommentError
import com.solve.domain.post.error.PostCommentReplyError
import com.solve.domain.post.error.PostCommentReplyLikeError
import com.solve.domain.post.error.PostError
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
    private val securityHolder: SecurityHolder
) : PostCommentReplyLikeService {
    @Transactional
    override fun likeCommentReply(postId: Long, commentId: Long, replyId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val comment = post.comments.find { it.id == commentId } ?: throw CustomException(PostCommentError.POST_COMMENT_NOT_FOUND, commentId)
        val reply = comment.replies.find { it.id == replyId } ?: throw CustomException(PostCommentReplyError.POST_COMMENT_REPLY_NOT_FOUND, replyId)
        val user = securityHolder.user

        if (reply.likes.any { it.user == user })
            throw CustomException(PostCommentReplyLikeError.POST_COMMENT_REPLY_ALREADY_LIKED, replyId)

        reply.likes.add(PostCommentReplyLike(
            reply = reply,
            user = user
        ))
    }

    @Transactional
    override fun unlikeCommentReply(postId: Long, commentId: Long, replyId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val comment = post.comments.find { it.id == commentId } ?: throw CustomException(PostCommentError.POST_COMMENT_NOT_FOUND, commentId)
        val reply = comment.replies.find { it.id == replyId } ?: throw CustomException(PostCommentReplyError.POST_COMMENT_REPLY_NOT_FOUND, replyId)
        val user = securityHolder.user
        val like = reply.likes.find { it.user == user } ?: throw CustomException(PostCommentReplyLikeError.POST_COMMENT_REPLY_NOT_LIKED, replyId)

        reply.likes.remove(like)
    }
}