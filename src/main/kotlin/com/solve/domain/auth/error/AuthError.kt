package com.solve.domain.auth.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class AuthError(override val status: HttpStatus, override val message: String) : CustomError {
    INVALID_VERIFICATION_TOKEN(HttpStatus.BAD_REQUEST, "Invalid verification token")
}