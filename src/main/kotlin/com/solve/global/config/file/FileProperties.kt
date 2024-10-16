package com.solve.global.config.file

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.file")
class FileProperties(
    val path: String
)