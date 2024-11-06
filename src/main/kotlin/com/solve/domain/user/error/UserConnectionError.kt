package com.solve.domain.user.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class UserConnectionError(override val status: HttpStatus, override val message: String) : CustomError {
    CONNECTION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Connection of type %s already exists"),
    CONNECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Connection not found")
}