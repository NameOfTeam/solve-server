package com.solve.domain.workbook.repository.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.solve.domain.workbook.domain.entity.QWorkbook
import com.solve.domain.workbook.domain.entity.QWorkbookBookmark
import com.solve.domain.workbook.domain.entity.QWorkbookLike
import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.domain.enums.WorkbookSearchFilter
import com.solve.domain.workbook.repository.WorkbookQueryRepository
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class WorkbookQueryRepositoryImpl(
    private val securityHolder: SecurityHolder,
    private val queryFactory: JPAQueryFactory
): WorkbookQueryRepository {
    private val workbook = QWorkbook.workbook
    private val bookmark = QWorkbookBookmark.workbookBookmark
    private val like = QWorkbookLike.workbookLike

    @Transactional(readOnly = true)
    override fun searchWorkbook(query: String, filter: WorkbookSearchFilter?, pageable: Pageable): Page<Workbook> {
        val baseQuery = queryFactory
            .selectFrom(workbook)
            .distinct()
            .where(
                containsSearchKeyword(query),
                filterByBookmark(filter)
            )

        applyOrdering(baseQuery, filter)

        val workbooks = baseQuery
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val totalCount = queryFactory
            .select(workbook.count())
            .from(workbook)
            .where(
                containsSearchKeyword(query),
                filterByBookmark(filter)
            )
            .fetchOne() ?: 0L

        return PageImpl(workbooks, pageable, totalCount)
    }

    private fun containsSearchKeyword(query: String): BooleanExpression? = if (query.isNotBlank()) workbook.title.containsIgnoreCase(query).or(workbook.description.containsIgnoreCase(query)) else null

    private fun filterByBookmark(filter: WorkbookSearchFilter?): BooleanExpression? =
        if (filter == WorkbookSearchFilter.BOOKMARKED && securityHolder.isAuthenticated) {
            workbook.id.`in`(
                queryFactory
                    .select(bookmark.workbook.id)
                    .from(bookmark)
                    .where(bookmark.user.eq(securityHolder.user))
            )
        } else null

    private fun applyOrdering(query: JPAQuery<Workbook>, filter: WorkbookSearchFilter?) {
        when (filter) {
            WorkbookSearchFilter.POPULAR -> {
                query.leftJoin(workbook.likes, like)
                    .groupBy(workbook)
                    .orderBy(like.count().desc())
            }
            else -> {
                query.orderBy(workbook._createdAt.desc())
            }
        }
    }
}