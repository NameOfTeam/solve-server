package com.solve.global.config

import com.solve.global.security.jwt.filter.JwtAuthenticationFilter
import com.solve.global.security.jwt.filter.JwtExceptionFilter
import com.solve.global.security.jwt.handler.JwtAccessDeniedHandler
import com.solve.global.security.jwt.handler.JwtAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtExceptionFilter: JwtExceptionFilter
) {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun roleHierarchy(): RoleHierarchy = RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER")

    @Bean
    fun filterChain(
        http: HttpSecurity
    ): SecurityFilterChain = http
        .csrf { it.disable() }
        .cors { it.configurationSource(corsConfigurationSource()) }
        .formLogin { it.disable() }
        .httpBasic { it.disable() }
        .rememberMe { it.disable() }
        .logout { it.disable() }

        .exceptionHandling {
            it.accessDeniedHandler(jwtAccessDeniedHandler)
            it.authenticationEntryPoint(jwtAuthenticationEntryPoint)
        }

        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        .authorizeHttpRequests {
            it
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**", "/api-docs").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/signup", "/auth/reissue").anonymous()

                .requestMatchers(HttpMethod.GET, "/problems", "/problems/{problemId}").permitAll()
                .requestMatchers(HttpMethod.POST, "/problems").admin()
                .requestMatchers(HttpMethod.PATCH, "/problems/{problemId}").admin()
                .requestMatchers(HttpMethod.DELETE, "/problems/{problemId}").admin()

                .requestMatchers(HttpMethod.GET, "/problems/{problemId}/test-cases").permitAll()
                .requestMatchers(HttpMethod.POST, "/problems/{problemId}/test-cases").admin()
                .requestMatchers(HttpMethod.PATCH, "/problems/{problemId}/test-cases/{testCaseId}").admin()
                .requestMatchers(HttpMethod.DELETE, "/problems/{problemId}/test-cases/{testCaseId}").admin()

                .requestMatchers(HttpMethod.POST, "/problems/{problemId}/submit").permitAll()

                .requestMatchers(HttpMethod.GET, "/ws").permitAll()

                .anyRequest().authenticated()
        }

        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        .addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter::class.java)

        .build()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource = UrlBasedCorsConfigurationSource().apply {
        registerCorsConfiguration("/**", CorsConfiguration().apply {
            allowedOriginPatterns = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
            allowedHeaders = listOf("*")
            allowCredentials = true
            maxAge = 3600
        })
    }

    private fun AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl.user() = permitAll()
    private fun AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl.admin() = hasRole("ADMIN")
}