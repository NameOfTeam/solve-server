package com.solve.global.error

import org.springframework.http.HttpStatus

enum class GlobalError(override val status: HttpStatus, override val message: String) : CustomError {
    NO_HANDLER_FOUND(HttpStatus.NOT_FOUND, "No handler found"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "No resource found")
}