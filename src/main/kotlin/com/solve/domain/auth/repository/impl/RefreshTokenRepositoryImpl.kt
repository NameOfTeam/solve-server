package com.solve.domain.auth.repository.impl

import com.solve.domain.auth.repository.RefreshTokenRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>
) : RefreshTokenRepository {
    override fun setRefreshToken(email: String, refreshToken: String) {
        redisTemplate.opsForValue().set("refreshToken:$email", refreshToken)
    }

    override fun getRefreshToken(email: String): String? {
        return redisTemplate.opsForValue().get("refreshToken:$email")
    }

    override fun existsRefreshToken(email: String): Boolean {
        return redisTemplate.hasKey("refreshToken:$email")
    }
}