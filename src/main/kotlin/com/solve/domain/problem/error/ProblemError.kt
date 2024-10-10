package com.solve.domain.problem.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class ProblemError(override val status: HttpStatus, override val message: String) : CustomError {
    PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "문제를 찾을 수 없습니다."),
    PROBLEM_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "문제에 대한 권한이 없습니다."),
    TEST_CASE_NOT_FOUND(HttpStatus.NOT_FOUND, "테스트 케이스를 찾을 수 없습니다."),
    LANGUAGE_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "지원하지 않는 언어입니다."),
}