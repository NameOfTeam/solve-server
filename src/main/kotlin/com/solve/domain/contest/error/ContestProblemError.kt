package com.solve.domain.contest.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class ContestProblemError(override val status: HttpStatus, override val message: String) : CustomError {
    CONTEST_PROBLEM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Contest problem already exists"),
    CONTEST_PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "Contest problem not found")
}