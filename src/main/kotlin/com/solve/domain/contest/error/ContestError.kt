package com.solve.domain.contest.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class ContestError(override val status: HttpStatus, override val message: String) : CustomError {
    CONTEST_NOT_FOUND(HttpStatus.NOT_FOUND, "대회를 찾을 수 없습니다."),
    CONTEST_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "대회에 권한이 없습니다."),
}