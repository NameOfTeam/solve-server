package com.solve.domain.workbook.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class WorkbookError(override val status: HttpStatus, override val message: String) : CustomError {
    WORKBOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "Workbook not found")
}