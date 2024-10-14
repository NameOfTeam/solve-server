package com.solve.global.config

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableRedisRepositories
class RedisConfig(
    private val redisProperties: RedisProperties,
//    private val verificationTokenExpiredEventListener: VerificationTokenExpiredEventListener
) {
    @Bean
    fun redisConnectionFactory() = LettuceConnectionFactory(
        RedisStandaloneConfiguration().apply {
            hostName = redisProperties.host
            port = redisProperties.port
            password = RedisPassword.of(redisProperties.password)
        }
    )

    @Bean
    fun redisTemplate() = RedisTemplate<String, String>().apply {
        connectionFactory = redisConnectionFactory()
        keySerializer = StringRedisSerializer()
        valueSerializer = StringRedisSerializer()
    }
}