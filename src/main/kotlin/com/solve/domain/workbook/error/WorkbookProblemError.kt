package com.solve.domain.workbook.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class WorkbookProblemError(override val status: HttpStatus, override val message: String): CustomError {
    WORKBOOK_PROBLEM_NOT_FOUND(HttpStatus.NOT_FOUND, "Workbook problem not found"),
    WORKBOOK_PROBLEM_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Workbook problem already exists")
}