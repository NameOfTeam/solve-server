package com.solve.global.config.admin

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.admin")
data class AdminProperties(
    val key: String,
)