package com.solve.domain.admin.contest.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class AdminContestError(override val status: HttpStatus, override val message: String) : CustomError {
    CONTEST_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "대회에 대한 권한이 없습니다."),
}