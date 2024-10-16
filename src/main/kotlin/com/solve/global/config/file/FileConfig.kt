package com.solve.global.config.file

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class FileConfig(
    private val fileProperties: FileProperties
) : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:${fileProperties.path}/uploads/")
        registry.addResourceHandler("/avatars/**")
            .addResourceLocations("file:${fileProperties.path}/avatars/")
    }
}