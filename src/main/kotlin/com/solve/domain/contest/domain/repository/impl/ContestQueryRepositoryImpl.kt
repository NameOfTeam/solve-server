package com.solve.domain.contest.domain.repository.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.entity.QContest
import com.solve.domain.contest.domain.enums.ContestSearchState
import com.solve.domain.contest.domain.repository.ContestQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
class ContestQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ContestQueryRepository {
    private val contest = QContest.contest

    @Transactional(readOnly = true)
    override fun searchContest(query: String, state: ContestSearchState?, pageable: Pageable): Page<Contest> {
        val now = LocalDateTime.now()

        val baseQuery = queryFactory
            .selectFrom(contest)
            .distinct()
            .where(
                containsSearchKeyword(query),
                filterByState(state, now)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        baseQuery.orderBy(contest.startTime.desc())

        val contests = baseQuery.fetch()
        val totalCount = queryFactory
            .select(contest.count())
            .from(contest)
            .where(
                containsSearchKeyword(query),
                filterByState(state, now)
            )
            .fetchOne() ?: 0L

        return PageImpl(contests, pageable, totalCount)
    }

    private fun containsSearchKeyword(query: String): BooleanExpression? =
        if (query.isNotBlank()) {
            contest.title.containsIgnoreCase(query)
                .or(contest.description.containsIgnoreCase(query))
        } else null

    private fun filterByState(state: ContestSearchState?, now: LocalDateTime): BooleanExpression? =
        when (state) {
            ContestSearchState.UPCOMING -> contest.startTime.after(now)
            ContestSearchState.ONGOING -> contest.startTime.before(now).and(contest.endTime.after(now))
            ContestSearchState.ENDED -> contest.endTime.before(now)
            null -> null
        }
}