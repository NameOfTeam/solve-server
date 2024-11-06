package com.solve.domain.admin.user.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class AdminUserConnectionError(override val status: HttpStatus, override val message: String) : CustomError {
    USER_CONNECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "User connection not found")
}