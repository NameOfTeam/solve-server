package com.solve.domain.problem.dto.response

import com.solve.domain.problem.domain.entity.ProblemIdeaComment
import com.solve.domain.user.domain.entity.User
import java.time.LocalDateTime
import java.util.*

data class ProblemIdeaCommentResponse(
    val id: Long,
    val content: String,
    val author: ProblemIdeaCommentAuthorResponse,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val isAuthor: Boolean = false,
    val children: List<ProblemIdeaCommentResponse>
) {
    companion object {
        fun of(comment: ProblemIdeaComment): ProblemIdeaCommentResponse {
            return ProblemIdeaCommentResponse(
                id = comment.id!!,
                content = comment.content,
                author = ProblemIdeaCommentAuthorResponse.of(comment.author),
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
                children = comment.children.map { of(it) }
            )
        }
    }
}

data class ProblemIdeaCommentAuthorResponse(
    val id: UUID,
    val username: String
) {
    companion object {
        fun of(author: User) = ProblemIdeaCommentAuthorResponse(
            id = author.id!!,
            username = author.username
        )
    }
}
