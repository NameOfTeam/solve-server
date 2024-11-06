package com.solve.global.error

import com.solve.global.config.discord.DiscordProperties
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.MethodNotAllowedException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RestControllerAdvice
class GlobalExceptionHandler(
    private val discordProperties: DiscordProperties,
    private val environment: Environment
) {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException) = ErrorResponse.of(e)

    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFoundException(e: NoHandlerFoundException) =
        ErrorResponse.of(CustomException(GlobalError.NO_HANDLER_FOUND, e.requestURL))

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(e: NoResourceFoundException) =
        ErrorResponse.of(CustomException(GlobalError.NO_RESOURCE_FOUND, e.resourcePath))

    @ExceptionHandler(MethodNotAllowedException::class)
    fun handleMethodNotAllowedException(e: MethodNotAllowedException) =
        ErrorResponse.of(CustomException(GlobalError.METHOD_NOT_ALLOWED))

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(e: HttpRequestMethodNotSupportedException) =
        ErrorResponse.of(CustomException(GlobalError.METHOD_NOT_ALLOWED))

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(e: HttpMessageNotReadableException) =
        ErrorResponse.of(CustomException(GlobalError.HTTP_MESSAGE_NOT_READABLE))

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        sendException(e)

        return ErrorResponse.of(CustomException(GlobalError.INTERNAL_SERVER_ERROR))
    }

    data class DiscordWebhookMessage(
        val embeds: List<Embed>
    )

    data class Embed(
        val title: String,
        val color: Int,
        val fields: List<Field>,
        val footer: Footer,
        val timestamp: String
    )

    data class Field(
        val name: String,
        val value: String,
        val inline: Boolean = false
    )

    data class Footer(
        val text: String
    )

    private fun sendException(exception: Exception) {
        val currentTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

        val webhookMessage = DiscordWebhookMessage(
            embeds = listOf(
                Embed(
                    title = "🚨 Exception Detected",
                    color = 0xFF0000, // Red color
                    fields = listOf(
                        Field(
                            name = "Exception Type",
                            value = exception.javaClass.simpleName,
                            inline = true
                        ),
                        Field(
                            name = "Timestamp",
                            value = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                            inline = true
                        ),
                        Field(
                            name = "Active Profiles",
                            value = environment.activeProfiles.joinToString(", ").ifBlank { "default" },
                            inline = true
                        ),
                        Field(
                            name = "Message",
                            value = exception.message ?: "No message",
                            inline = false
                        ),
                        Field(
                            name = "Stack Trace",
                            value = "```${
                                exception.stackTrace.take(3).joinToString("\n") {
                                    "at ${it.className}.${it.methodName}(${it.fileName}:${it.lineNumber})"
                                }
                            }```",
                            inline = false
                        )
                    ),
                    footer = Footer("Solve Server"),
                    timestamp = currentTime.format(DateTimeFormatter.ISO_DATE_TIME)
                )
            )
        )

        try {
            WebClient.create()
                .post()
                .uri(discordProperties.webhook.exception.url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(webhookMessage)
                .retrieve()
                .toBodilessEntity()
                .block()
        } catch (e: Exception) {
            println("Failed to send exception to Discord webhook: ${e.message}")
        }
    }

}