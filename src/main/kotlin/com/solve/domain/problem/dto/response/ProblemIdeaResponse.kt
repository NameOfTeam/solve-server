package com.solve.domain.problem.dto.response

import com.solve.domain.problem.domain.entity.ProblemIdea
import com.solve.domain.user.domain.entity.User
import java.util.*

data class ProblemIdeaResponse(
    val id: Long,
    val title: String,
    val content: String,
    val author: ProblemIdeaAuthorResponse,
    val comments: List<ProblemIdeaCommentResponse>,
    val likeCount: Int,
    var isLiked: Boolean = false,
    var isAuthor: Boolean = false
) {
    companion object {
        fun of(idea: ProblemIdea) = ProblemIdeaResponse(
            id = idea.id!!,
            title = idea.title,
            content = idea.content,
            author = ProblemIdeaAuthorResponse.of(idea.author),
            comments = idea.comments.map { ProblemIdeaCommentResponse.of(it) },
            likeCount = idea.likes.size,
        )
    }
}

data class ProblemIdeaAuthorResponse(
    val id: UUID,
    val username: String
) {
    companion object {
        fun of(author: User) = ProblemIdeaAuthorResponse(
            id = author.id!!,
            username = author.username
        )
    }
}