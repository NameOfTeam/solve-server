package com.solve.domain.auth.repository.impl

import com.solve.domain.auth.repository.VerificationTokenRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class VerificationTokenRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>
) : VerificationTokenRepository {
    override fun setVerificationToken(verificationToken: String, email: String) {
        redisTemplate.opsForValue().set("verificationToken:$verificationToken", email, 5, TimeUnit.MINUTES)
    }

    override fun getVerificationToken(verificationToken: String): String? {
        return redisTemplate.opsForValue().get("verificationToken:$verificationToken")
    }

    override fun existsVerificationToken(verificationToken: String): Boolean {
        return redisTemplate.hasKey("verificationToken:$verificationToken")
    }

    override fun deleteVerificationToken(verificationToken: String) {
        redisTemplate.delete("verificationToken:$verificationToken")
    }
}