package com.solve.domain.workbook.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class WorkbookLikeError(override val status: HttpStatus, override val message: String) : CustomError {
    WORKBOOK_LIKE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 문제집입니다."),
    WORKBOOK_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 누른 문제집을 찾을 수 없습니다.")
}