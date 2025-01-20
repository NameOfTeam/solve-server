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

                .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/signup", "/auth/reissue", "/auth/verify").anonymous()
                .requestMatchers(HttpMethod.POST, "/admin/auth/signup").anonymous()

                .requestMatchers(HttpMethod.GET, "/statistics").permitAll()

                .requestMatchers(HttpMethod.GET, "/posts/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/posts", "/posts/{postId}").permitAll()
                .requestMatchers(HttpMethod.POST, "/posts").user()
                .requestMatchers(HttpMethod.PATCH, "/posts/{postId}").user()
                .requestMatchers(HttpMethod.DELETE, "/posts/{postId}").user()

                .requestMatchers(HttpMethod.GET, "/posts/{postId}/comments").permitAll()
                .requestMatchers(HttpMethod.POST, "/posts/{postId}/comments").user()
                .requestMatchers(HttpMethod.PATCH, "/posts/{postId}/comments/{commentId}").user()
                .requestMatchers(HttpMethod.DELETE, "/posts/{postId}/comments/{commentId}").user()

                .requestMatchers(HttpMethod.GET, "/posts/{postId}/comments/{commentId}/replies").permitAll()
                .requestMatchers(HttpMethod.POST, "/posts/{postId}/comments/{commentId}/replies").user()
                .requestMatchers(HttpMethod.PATCH, "/posts/{postId}/comments/{commentId}/replies/{replyId}").user()
                .requestMatchers(HttpMethod.DELETE, "/posts/{postId}/comments/{commentId}/replies/{replyId}").user()

                .requestMatchers(HttpMethod.POST, "/posts/{postId}/comments/{commentId}/likes").user()
                .requestMatchers(HttpMethod.DELETE, "/posts/{postId}/comments/{commentId}/likes").user()
                .requestMatchers(HttpMethod.POST, "/posts/{postId}/comments/{commentId}/replies/{replyId}/likes").user()
                .requestMatchers(HttpMethod.DELETE, "/posts/{postId}/comments/{commentId}/replies/{replyId}/likes").user()

                .requestMatchers(HttpMethod.GET, "/problems", "/problems/{problemId}", "/problems/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/problems/{problemId}/code").user()
                .requestMatchers(HttpMethod.POST, "/problems/{problemId}/code").user()
                .requestMatchers(HttpMethod.DELETE, "/problems/{problemId}/code").user()

                .requestMatchers(HttpMethod.POST, "/submits").user()
                .requestMatchers(HttpMethod.GET, "/submits/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/submits/my").user()

                .requestMatchers(HttpMethod.POST, "/runs").user()

                .requestMatchers(HttpMethod.GET, "/users/search").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/{username}").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/me").user()
                .requestMatchers(HttpMethod.PATCH, "/users/me").user()
                .requestMatchers(HttpMethod.PATCH, "/users/me/password").user()
                .requestMatchers(HttpMethod.PATCH, "/users/me/avatar").user()
                .requestMatchers(HttpMethod.GET, "/users/me/solved").user()

                .requestMatchers(HttpMethod.POST, "/users/me/connections").user()
                .requestMatchers(HttpMethod.DELETE, "/users/me/connections/{connectionId}").user()

                .requestMatchers(HttpMethod.GET, "/workbooks", "/workbooks/{workbookId}", "/workbooks/search").permitAll()
                .requestMatchers(HttpMethod.POST, "/workbooks").user()
                .requestMatchers(HttpMethod.PATCH, "/workbooks/{workbookId}").user()
                .requestMatchers(HttpMethod.DELETE, "/workbooks/{workbookId}").user()

                .requestMatchers(HttpMethod.POST, "/workbooks/{workbookId}/problems").user()
                .requestMatchers(HttpMethod.DELETE, "/workbooks/{workbookId}/problems/{problemId}").user()

                .requestMatchers(HttpMethod.POST, "/workbooks/{workbookId}/likes").user()
                .requestMatchers(HttpMethod.DELETE, "/workbooks/{workbookId}/likes").user()
                .requestMatchers(HttpMethod.POST, "/workbooks/{workbookId}/bookmarks").user()
                .requestMatchers(HttpMethod.DELETE, "/workbooks/{workbookId}/bookmarks").user()

                .requestMatchers(HttpMethod.GET, "/contests", "/contests/{contestId}", "/contests/search").permitAll()
                .requestMatchers(HttpMethod.POST, "/contests/{contestId}/participants").user()
                .requestMatchers(HttpMethod.DELETE, "/contests/{contestId}/participants").user()
                .requestMatchers(HttpMethod.POST, "/contests/{contestId}/problems").user()
                .requestMatchers(HttpMethod.DELETE, "/contests/{contestId}/problems/{problemId}").user()

                .requestMatchers(HttpMethod.GET, "/templates/{language}").permitAll()
                .requestMatchers(HttpMethod.GET, "/themes", "/themes/search").permitAll()

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
            allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
            maxAge = 3600
        })
    }

    private fun AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl.user() = hasRole("USER")
    private fun AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizedUrl.admin() = hasRole("ADMIN")
}