package com.solve.global.config.redis

import com.solve.domain.submit.listener.ProgressListener
import com.solve.domain.submit.listener.ResultListener
import com.solve.domain.submit.service.SubmitService
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(
    @Value("\${spring.redis.host:localhost}")
    private val host: String,

    @Value("\${spring.redis.port:6379}")
    private val port: Int
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(host, port)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory()
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = StringRedisSerializer()
        return template
    }

    @Bean
    fun stringRedisTemplate(): StringRedisTemplate {
        val template = StringRedisTemplate()
        template.connectionFactory = redisConnectionFactory()
        return template
    }

    @Bean
    fun redisMessageListenerContainer(
        connectionFactory: RedisConnectionFactory,
        progressListener: ProgressListener,
        resultListener: ResultListener
    ): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(connectionFactory)

        // 채점 진행 상황 리스너 등록
        container.addMessageListener(
            progressListener,
            PatternTopic(SubmitService.PROGRESS_CHANNEL)
        )

        // 채점 결과 리스너 등록
        container.addMessageListener(
            resultListener,
            PatternTopic(SubmitService.RESULT_CHANNEL)
        )

        return container
    }
}