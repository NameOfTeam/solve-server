package com.solve.domain.user.repository.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.solve.domain.user.domain.entity.QUser
import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.repository.UserQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class UserQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : UserQueryRepository {
    private val user = QUser.user

    @Transactional(readOnly = true)
    override fun searchUser(query: String, pageable: Pageable): Page<User> {
        val baseQuery = queryFactory
            .selectFrom(user)
            .distinct()
            .where(
                containsSearchKeyword(query)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        baseQuery.orderBy(user._createdAt.desc())

        val users = baseQuery.fetch()
        val totalCount = queryFactory
            .select(user.count())
            .from(user)
            .where(
                containsSearchKeyword(query)
            )
            .fetchOne() ?: 0L

        return PageImpl(users, pageable, totalCount)
    }

    private fun containsSearchKeyword(query: String): BooleanExpression? =
        if (query.isNotBlank()) {
            user.username.containsIgnoreCase(query)
                .or(user.email.containsIgnoreCase(query))
                .or(user.introduction.containsIgnoreCase(query))
        } else null
}