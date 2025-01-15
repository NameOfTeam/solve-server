package com.solve.domain.post.service.impl

import com.solve.domain.post.domain.entity.PostCommentReply
import com.solve.domain.post.dto.request.PostCommentReplyCreateRequest
import com.solve.domain.post.dto.request.PostCommentReplyUpdateRequest
import com.solve.domain.post.dto.response.PostCommentReplyAuthorResponse
import com.solve.domain.post.dto.response.PostCommentReplyResponse
import com.solve.domain.post.error.PostCommentError
import com.solve.domain.post.error.PostCommentReplyError
import com.solve.domain.post.error.PostError
import com.solve.domain.post.repository.PostRepository
import com.solve.domain.post.service.PostCommentReplyService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostCommentReplyServiceImpl(
    private val postRepository: PostRepository,
    private val securityHolder: SecurityHolder
) : PostCommentReplyService {
    @Transactional(readOnly = true)
    override fun getReplies(postId: Long, commentId: Long): List<PostCommentReplyResponse> {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND)
        val comment = post.comments.find { it.id == commentId } ?: throw CustomException(PostCommentError.POST_COMMENT_NOT_FOUND)

        return comment.replies.map { it.toResponse() }
    }

    @Transactional
    override fun createReply(postId: Long, commentId: Long, request: PostCommentReplyCreateRequest) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND)
        val comment = post.comments.find { it.id == commentId } ?: throw CustomException(PostCommentError.POST_COMMENT_NOT_FOUND)
        val author = securityHolder.user
        val reply = request.replyId?.let { comment.replies.find { reply -> reply.id == it } }

        comment.replies.add(PostCommentReply(
            content = request.content,
            author = author,
            post = post,
            comment = comment,
            reply = reply
        ))
    }

    @Transactional
    override fun updateReply(postId: Long, commentId: Long, replyId: Long, request: PostCommentReplyUpdateRequest) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND)
        val comment = post.comments.find { it.id == commentId } ?: throw CustomException(PostCommentError.POST_COMMENT_NOT_FOUND)
        val reply = comment.replies.find { it.id == replyId } ?: throw CustomException(PostCommentReplyError.POST_COMMENT_REPLY_NOT_FOUND)

        if (reply.author != securityHolder.user) throw CustomException(PostCommentReplyError.POST_COMMENT_REPLY_NOT_AUTHORIZED)

        request.content?.let { reply.content = it }
    }

    @Transactional
    override fun deleteReply(postId: Long, commentId: Long, replyId: Long) {
        val post = postRepository.findByIdOrNull(postId) ?: throw CustomException(PostError.POST_NOT_FOUND, postId)
        val comment = post.comments.find { it.id == commentId } ?: throw CustomException(PostCommentError.POST_COMMENT_NOT_FOUND, commentId)
        val reply = comment.replies.find { it.id == replyId } ?: throw CustomException(PostCommentReplyError.POST_COMMENT_REPLY_NOT_FOUND, replyId)

        if (reply.author != securityHolder.user) throw CustomException(PostCommentReplyError.POST_COMMENT_REPLY_NOT_AUTHORIZED)

        comment.replies.remove(reply)
    }

    private fun PostCommentReply.toResponse() = PostCommentReplyResponse(
        id = id!!,
        content = content,
        author = PostCommentReplyAuthorResponse.of(author),
        likeCount = likes.size,
        liked = likes.any { it.user == securityHolder.user },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}