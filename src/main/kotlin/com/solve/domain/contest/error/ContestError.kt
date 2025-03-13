package com.solve.domain.contest.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class ContestError(override val status: HttpStatus, override val message: String) : CustomError {
    CONTEST_NOT_FOUND(HttpStatus.NOT_FOUND, "대회를 찾을 수 없습니다."),
    CONTEST_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "대회에 권한이 없습니다."),
    CONTEST_REGISTRATION_NOT_OPEN(HttpStatus.BAD_REQUEST, "대회 참가가 열리지 않았습니다."),
    CONTEST_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 대회에 참가하였습니다."),

    CONTEST_PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "대회 참가자를 찾을 수 없습니다."),
    CONTEST_ANNOUNCEMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "대회 공지를 찾을 수 없습니다."),
    CONTEST_ANNOUNCEMENT_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "대회 공지에 권한이 없습니다."),
}