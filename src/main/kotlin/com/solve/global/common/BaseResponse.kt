package com.devox.global.common

import org.springframework.http.ResponseEntity

data class BaseResponse<T>(
    val data: T,
    val status: Int,
    val message: String,
    val success: Boolean
) {
    companion object {
        fun <T> of(data: T, status: Int = 200, message: String = "success", success: Boolean = true) =
            ResponseEntity.status(status)
                .body(BaseResponse(data = data, status = status, message = message, success = success))
    }
}