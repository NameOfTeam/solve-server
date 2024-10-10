package com.devox.global.error

import org.springframework.http.ResponseEntity

data class ErrorResponse(
    val code: String,
    val status: Int,
    val message: String
) {
    companion object {
        fun of(exception: CustomException) = ResponseEntity.status(exception.status).body(
            ErrorResponse(
                code = exception.code,
                status = exception.status,
                message = exception.message
            )
        )
    }
}
