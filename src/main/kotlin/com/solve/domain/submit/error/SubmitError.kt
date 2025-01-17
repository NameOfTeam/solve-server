package com.solve.domain.submit.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class SubmitError(override val status: HttpStatus, override val message: String) : CustomError {
    SUBMIT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 제출을 찾을 수 없습니다."),
}