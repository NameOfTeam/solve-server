package com.solve.domain.user.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class UserError(override val status: HttpStatus, override val message: String) : CustomError {
    USER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다. (이메일: %s)"),

    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
}