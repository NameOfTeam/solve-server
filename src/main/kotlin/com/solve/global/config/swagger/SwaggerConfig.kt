package com.solve.global.config.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@Configuration
class SwaggerConfig {
    @Bean
    fun api(): OpenAPI =
        OpenAPI().info(Info().title("Solve").description("Solve API Documentation").version("v1.0"))
            .addSecurityItem(SecurityRequirement().addList("Authorization"))
            .servers(
                listOf(
                    Server().url("http://api.solve.jmino.me").description("Production Server"),
                    Server().url("http://localhost:8080").description("Development Server"),
                )
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "Authorization", SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("Bearer")
                            .bearerFormat("Authorization")
                            .`in`(SecurityScheme.In.HEADER)
                            .name(HttpHeaders.AUTHORIZATION)
                    )
            )
}