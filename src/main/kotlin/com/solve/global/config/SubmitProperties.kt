package com.solve.global.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.submit")
data class SubmitProperties(
    val path: String
)
