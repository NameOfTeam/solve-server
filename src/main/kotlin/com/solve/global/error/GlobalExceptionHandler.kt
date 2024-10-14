package com.solve.global.error

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.MethodNotAllowedException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException) = ErrorResponse.of(e)

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException) =
        ErrorResponse.of(CustomException(GlobalError.NO_HANDLER_FOUND))

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(e: NoResourceFoundException) =
        ErrorResponse.of(CustomException(GlobalError.NO_RESOURCE_FOUND))

    @ExceptionHandler(MethodNotAllowedException::class)
    fun handleMethodNotAllowedException(e: MethodNotAllowedException) =
        ErrorResponse.of(CustomException(GlobalError.METHOD_NOT_ALLOWED))

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        e.printStackTrace()

        return ErrorResponse.of(CustomException(GlobalError.INTERNAL_SERVER_ERROR))
    }
}