package com.solve.domain.post.dto.response

import com.solve.domain.post.domain.enums.PostCategory
import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.user.domain.entity.User
import com.solve.global.common.enums.ProgrammingLanguage
import java.time.LocalDateTime
import java.util.*

data class PostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val category: PostCategory,
    val language: ProgrammingLanguage?,
    val author: PostAuthorResponse,
    val problem: PostProblemResponse?,
    val likeCount: Long,
    val isLiked: Boolean,
    val commentCount: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class PostAuthorResponse(
    val id: UUID,
    val username: String
) {
    companion object {
        fun of(author: User) = PostAuthorResponse(
            id = author.id!!,
            username = author.username
        )
    }
}

data class PostProblemResponse(
    val id: Long
) {
    companion object {
        fun of(problem: Problem) = PostProblemResponse(
            id = problem.id!!
        )
    }
}