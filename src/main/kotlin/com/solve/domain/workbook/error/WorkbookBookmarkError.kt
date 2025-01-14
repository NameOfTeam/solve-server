package com.solve.domain.workbook.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class WorkbookBookmarkError(override val status: HttpStatus, override val message: String): CustomError {
    WORKBOOK_BOOKMARK_NOT_FOUND(HttpStatus.NOT_FOUND, "Workbook bookmark not found"),
    WORKBOOK_BOOKMARK_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Workbook bookmark already exists")
}