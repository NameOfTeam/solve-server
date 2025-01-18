package com.solve.domain.theme.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.solve.domain.theme.domain.entity.QTheme
import com.solve.domain.theme.domain.entity.Theme
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ThemeQueryRepository(
    private val queryFactory: JPAQueryFactory
) {
    private val theme = QTheme.theme

    @Transactional(readOnly = true)
    fun searchTheme(query: String, pageable: Pageable): Page<Theme> {
        val contents = queryFactory
            .selectFrom(theme)
            .distinct()
            .where(
                searchKeywordContains(query)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .orderBy(theme._createdAt.desc(), theme.id.desc())
            .fetch()

        val total = queryFactory
            .select(theme.count())
            .from(theme)
            .where(
                searchKeywordContains(query)
            )
            .fetchOne() ?: 0L

        return PageImpl(contents, pageable, total)
    }

    private fun searchKeywordContains(query: String): BooleanExpression? =
        if (query.isBlank()) {
            null
        } else {
            theme.name.containsIgnoreCase(query)
                .or(theme.description.containsIgnoreCase(query))
        }
}