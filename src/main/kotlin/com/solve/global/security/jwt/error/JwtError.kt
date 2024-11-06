package com.solve.global.security.jwt.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class JwtError(override val status: HttpStatus, override val message: String) : CustomError {
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 타입입니다."),
    INCORRECT_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
}