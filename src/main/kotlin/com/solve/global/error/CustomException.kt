package com.devox.global.error

class CustomException(val error: CustomError, vararg args: String) : RuntimeException() {
    val code = (error as Enum<*>).name
    val status = error.status.value()
    override val message = error.message.format(*args)
}