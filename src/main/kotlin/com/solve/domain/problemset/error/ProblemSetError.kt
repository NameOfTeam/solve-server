package com.solve.domain.problemset.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class ProblemSetError(override val status: HttpStatus, override val message: String) : CustomError {
    PROBLEM_SET_NOT_FOUND(HttpStatus.NOT_FOUND, "Problem set not found")
}