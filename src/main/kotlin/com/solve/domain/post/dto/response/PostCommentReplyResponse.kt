package com.solve.domain.post.dto.response

import com.solve.domain.user.domain.entity.User
import java.time.LocalDateTime
import java.util.*

data class PostCommentReplyResponse(
    val id: Long,
    val content: String,
    val author: PostCommentReplyAuthorResponse,
    val likeCount: Long,
    val isLiked: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class PostCommentReplyAuthorResponse(
    val id: UUID,
    val username: String,
) {
    companion object {
        fun of(author: User) = PostCommentReplyAuthorResponse(
            id = author.id!!,
            username = author.username
        )
    }
}