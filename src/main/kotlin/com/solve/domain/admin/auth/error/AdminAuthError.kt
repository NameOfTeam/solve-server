package com.solve.domain.admin.auth.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class AdminAuthError(override val status: HttpStatus, override val message: String) : CustomError {
    INVALID_KEY(HttpStatus.BAD_REQUEST, "유효하지 않은 키입니다.")
}