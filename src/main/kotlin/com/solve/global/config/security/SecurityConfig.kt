package com.solve.global.config.security

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

                .requestMatchers(HttpMethod.GET, "/problems/{problemId}/test-cases").permitAll()

                .requestMatchers(HttpMethod.GET, "/problems/{problemId}/ideas").permitAll()
                .requestMatchers(HttpMethod.POST, "/problems/{problemId}/ideas").user()
                .requestMatchers(HttpMethod.PATCH, "/problems/{problemId}/ideas").user()
                .requestMatchers(HttpMethod.DELETE, "/problems/{problemId}/ideas/{ideaId}").user()

                .requestMatchers(HttpMethod.GET, "/problems/{problemId}/ideas/{ideaId}/comments").permitAll()
                .requestMatchers(HttpMethod.POST, "/problems/{problemId}/ideas/{ideaId}/comments").user()
                .requestMatchers(HttpMethod.PATCH, "/problems/{problemId}/ideas/{ideaId}/comments/{commentId}").user()
                .requestMatchers(HttpMethod.DELETE, "/problems/{problemId}/ideas/{ideaId}/comments/{commentId}").user()

                .requestMatchers(HttpMethod.POST, "/problems/{problemId}/submit").user()

                .requestMatchers(HttpMethod.GET, "/users/me").user()
                .requestMatchers(HttpMethod.PATCH, "/users/me").user()

                .requestMatchers(HttpMethod.GET, "/ws").permitAll()

                .requestMatchers(HttpMethod.POST, "/admin/auth/signup").anonymous()
                .requestMatchers("/admin/**").admin()

                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers("/avatars/**").permitAll()

                .anyRequest().permitAll()
        }

        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        .addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter::class.java)

        .build()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource = UrlBasedCorsConfigurationSource().apply {
        registerCorsConfiguration("/**", CorsConfiguration().apply {
            allowedOriginPatterns = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE")
            allowedHeaders = listOf("*")
            allowCredentials = true
            maxAge = 3600
        })
    }

    private fun AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl.user() = hasRole("USER")
    private fun AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl.admin() = hasRole("ADMIN")
}