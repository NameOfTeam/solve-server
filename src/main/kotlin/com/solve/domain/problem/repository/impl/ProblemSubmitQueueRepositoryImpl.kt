package com.solve.domain.problem.repository.impl

import com.solve.domain.problem.repository.ProblemSubmitQueueRepository
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class ProblemSubmitQueueRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>
) : ProblemSubmitQueueRepository {
    override fun push(submitId: Long) {
        redisTemplate.opsForList().leftPush("problemSubmitQueue", submitId.toString())
    }

    override fun pop(): Long? {
        return redisTemplate.opsForList().rightPop("problemSubmitQueue")?.toLong()
    }

    override fun size(): Int {
        return redisTemplate.opsForList().size("problemSubmitQueue")?.toInt() ?: 0
    }
}