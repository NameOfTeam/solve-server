package com.solve.global.config.frontend

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.frontend")
data class FrontendProperties(
    val url: String
)