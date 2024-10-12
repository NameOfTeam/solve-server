package com.solve.domain.auth.repository.impl

import com.solve.domain.auth.repository.RefreshTokenRepository
import com.solve.global.security.jwt.config.JwtProperties
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RefreshTokenRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>,
    private val jwtProperties: JwtProperties
) : RefreshTokenRepository {
    override fun setRefreshToken(email: String, refreshToken: String) {
        redisTemplate.opsForValue()
            .set("refreshToken:$email", refreshToken, jwtProperties.refreshTokenExpiration, TimeUnit.MILLISECONDS)
    }

    override fun getRefreshToken(email: String): String? {
        return redisTemplate.opsForValue().get("refreshToken:$email")
    }

    override fun existsRefreshToken(email: String): Boolean {
        return redisTemplate.hasKey("refreshToken:$email")
    }
}