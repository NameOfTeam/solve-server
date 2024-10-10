package com.solve.global.error

import org.springframework.http.HttpStatus

enum class GlobalError(override val status: HttpStatus, override val message: String) : CustomError {
    NO_HANDLER_FOUND(HttpStatus.NOT_FOUND, "No handler found"),
}