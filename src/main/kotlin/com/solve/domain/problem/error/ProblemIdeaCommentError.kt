package com.solve.domain.problem.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class ProblemIdeaCommentError(override val status: HttpStatus, override val message: String) : CustomError {
    PROBLEM_IDEA_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "문제 아이디어 댓글을 찾을 수 없습니다."),
    PROBLEM_IDEA_COMMENT_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "문제 아이디어 댓글에 대한 권한이 없습니다.")
}