package com.solve.domain.problem.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class ProblemSubmitError(override val status: HttpStatus, override val message: String) : CustomError {
    PROBLEM_SUBMIT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 제출을 찾을 수 없습니다."),
}