package com.solve.domain.contest.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class ContestParticipantError(override val status: HttpStatus, override val message: String) : CustomError {
    // 이미 참가한 대회에 참가하려고 할 때
    CONTEST_PARTICIPANT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 참가한 대회입니다."),

    // 대회에 참가하지 않은 상태에서 대회를 떠날 때
    CONTEST_PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "대회에 참가하지 않았습니다."),

    // 대회가 이미 시작된 상태에서 참가하려고 할 때
    CONTEST_ALREADY_STARTED(HttpStatus.BAD_REQUEST, "대회가 이미 시작되었습니다."),

    // 대회가 이미 종료된 상태에서 참가하려고 할 때
    CONTEST_ALREADY_ENDED(HttpStatus.BAD_REQUEST, "대회가 이미 종료되었습니다."),

    // 비공개 대회에 참가하려고 할 때
    CONTEST_PRIVATE(HttpStatus.FORBIDDEN, "비공개 대회에 참가할 수 없습니다.")
}