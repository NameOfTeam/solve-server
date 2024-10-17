package com.solve.domain.contest.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class ContestOperatorError(override val status: HttpStatus, override val message: String) : CustomError {
    CONTEST_OPERATOR_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Contest operator already exists"),
    CONTEST_OPERATOR_NOT_FOUND(HttpStatus.NOT_FOUND, "Contest operator not found")
}