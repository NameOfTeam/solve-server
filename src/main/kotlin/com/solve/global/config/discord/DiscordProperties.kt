package com.solve.global.config.discord

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.discord")
data class DiscordProperties(
    val webhook: DiscordWebhook
) {
    data class DiscordWebhook(
        val exception: DiscordWebhookException
    )

    data class DiscordWebhookException(
        val url: String
    )
}

