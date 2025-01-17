package com.solve.domain.submit.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class SubmitQueueRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun push(submitId: Long) {
        redisTemplate.opsForList().leftPush("problemSubmitQueue", submitId.toString())
    }

    fun pop(): Long? {
        return redisTemplate.opsForList().rightPop("problemSubmitQueue")?.toLong()
    }

    fun size(): Int {
        return redisTemplate.opsForList().size("problemSubmitQueue")?.toInt() ?: 0
    }
}