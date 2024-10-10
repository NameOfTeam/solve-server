package com.devox.domain.user.error

import com.devox.global.error.CustomError
import org.springframework.http.HttpStatus

enum class UserError(override val status: HttpStatus, override val message: String) : CustomError {
    USER_NOT_FOUND_BY_LINKUP_ID(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다. (링크업 아이디: %s)"),
    USER_NOT_FOUND_BY_EMAIL(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다. (이메일: %s)"),
    USER_NOT_FOUND_BY_PHONE_NUMBER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다. (휴대폰 번호: %s)"),
    LINKUP_ID_OR_PHONE_NUMBER_IS_NULL(HttpStatus.BAD_REQUEST, "Linkup Id 또는 휴대폰 번호가 Null입니다."),

    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 사용중인 이메일입니다."),
    PHONE_NUMBER_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 사용중인 전화번호입니다."),
    INVALID_BIRTHDAY(HttpStatus.BAD_REQUEST, "유효하지 않은 생년월일입니다."),
    LINKUP_ID_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 사용중인 링크업 아이디입니다."),
    LINKUP_ID_UPDATED_AT(HttpStatus.BAD_REQUEST, "링크업 아이디는 30일에 한 번만 변경할 수 있습니다.")
}