package com.solve.global.config.querydsl

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QueryDSLConfig {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Bean
    fun queryFactory() = JPAQueryFactory(entityManager)
}