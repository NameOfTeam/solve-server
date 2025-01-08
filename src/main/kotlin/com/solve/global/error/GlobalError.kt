package com.solve.global.error

import org.springframework.http.HttpStatus

enum class GlobalError(override val status: HttpStatus, override val message: String) : CustomError {
    NO_HANDLER_FOUND(HttpStatus.NOT_FOUND, "No handler found (url: %s)"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "No resource found (url: %s)"),
    HTTP_MESSAGE_NOT_READABLE(HttpStatus.BAD_REQUEST, "Http message not readable"),
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "Method argument type mismatch (name: %s, value: %s)"),
    METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "Method argument not valid"),
}