package com.solve.domain.problem.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class ProblemCodeError(override val status: HttpStatus, override val message: String) : CustomError {
    PROBLEM_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "저장된 코드가 없습니다."),
    PROBLEM_CODE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 저장된 코드가 있습니다.")
}