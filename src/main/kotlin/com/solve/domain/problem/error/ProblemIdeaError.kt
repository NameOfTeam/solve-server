package com.solve.domain.problem.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class ProblemIdeaError(override val status: HttpStatus, override val message: String) : CustomError {
    PROBLEM_IDEA_NOT_FOUND(HttpStatus.NOT_FOUND, "문제 아이디어를 찾을 수 없습니다."),
}