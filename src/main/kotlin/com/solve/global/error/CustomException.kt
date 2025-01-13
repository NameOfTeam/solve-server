package com.solve.global.error

class CustomException(val error: CustomError, vararg args: Any) : RuntimeException() {
    val code = (error as Enum<*>).name
    val status = error.status.value()
    override val message = error.message.format(*args)
}