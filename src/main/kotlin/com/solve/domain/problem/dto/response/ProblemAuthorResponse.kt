package com.solve.domain.problem.dto.response

import com.solve.domain.user.domain.entity.User

data class ProblemAuthorResponse(
    val username: String
) {
    companion object {
        fun of(author: User) = ProblemAuthorResponse(
            username = author.username
        )
    }
}