package com.solve.domain.admin.contest.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class AdminContestParticipantError(override val status: HttpStatus, override val message: String) : CustomError {
    CONTEST_PARTICIPANT_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Contest participant already exists"),
    CONTEST_PARTICIPANT_NOT_FOUND(HttpStatus.NOT_FOUND, "Contest participant not found")
}