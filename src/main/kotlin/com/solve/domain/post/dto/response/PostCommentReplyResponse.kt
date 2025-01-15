package com.solve.domain.post.dto.response

import com.solve.domain.user.domain.entity.User
import java.time.LocalDateTime
import java.util.UUID

data class PostCommentReplyResponse(
    val id: Long,
    val content: String,
    val author: PostCommentReplyAuthorResponse,
    val likeCount: Int,
    val liked: Boolean,
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