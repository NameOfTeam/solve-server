package com.solve.domain.problem.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class ProblemIdeaLikeError(override val status: HttpStatus, override val message: String) : CustomError {
    PROBLEM_IDEA_LIKE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 상태입니다."),
    PROBLEM_IDEA_LIKE_NOT_FOUND(HttpStatus.BAD_REQUEST, "좋아요를 누르지 않은 상태입니다.")
}