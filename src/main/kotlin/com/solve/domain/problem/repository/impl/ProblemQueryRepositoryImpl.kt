package com.solve.domain.problem.repository.impl

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.QProblem
import com.solve.domain.problem.domain.entity.QProblemSubmit
import com.solve.domain.problem.domain.enums.ProblemSearchOrder
import com.solve.domain.problem.domain.enums.ProblemSearchState
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.repository.ProblemQueryRepository
import com.solve.global.common.enums.Tier
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class ProblemQueryRepositoryImpl(
    private val securityHolder: SecurityHolder,
    private val queryFactory: JPAQueryFactory
) : ProblemQueryRepository {
    private val problem = QProblem.problem
    private val submit = QProblemSubmit.problemSubmit

    @Transactional(readOnly = true)
    override fun searchProblem(
        query: String,
        states: List<ProblemSearchState>,
        tiers: List<Tier>,
        order: ProblemSearchOrder,
        pageable: Pageable
    ): Page<Problem> {
        val baseQuery = queryFactory
            .selectFrom(problem)
            .distinct()
            .where(
                containsSearchKeyword(query),
                filterByTiers(tiers),
                filterByStates(states)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())

        applyOrdering(baseQuery, order)

        val problems = baseQuery.fetch()
        val totalCount = queryFactory
            .select(problem.count())
            .from(problem)
            .where(
                containsSearchKeyword(query),
                filterByTiers(tiers),
                filterByStates(states)
            )
            .fetchOne() ?: 0L

        return PageImpl(problems, pageable, totalCount)
    }

    private fun containsSearchKeyword(query: String): BooleanExpression? =
        if (query.isNotBlank()) problem.title.containsIgnoreCase(query)
            .or(problem.content.containsIgnoreCase(query)) else null

    private fun filterByTiers(tiers: List<Tier>): BooleanExpression? =
        if (tiers.isNotEmpty()) problem.tier.`in`(tiers) else null

    private fun filterByStates(state: List<ProblemSearchState>): BooleanExpression? {
        if (!securityHolder.isAuthenticated) return null

        val submits = queryFactory
            .selectFrom(submit)
            .where(submit.author.eq(securityHolder.user))
            .fetch()

        if (state.contains(ProblemSearchState.SOLVED)) {
            return problem.id.`in`(
                submits
                    .filter { it.state == ProblemSubmitState.ACCEPTED }
                    .map { it.problem.id }
            )
        }

        if (state.contains(ProblemSearchState.UNSOLVED)) {
            return problem.id.notIn(
                submits.map { it.problem.id }
            )
        }

        if (state.contains(ProblemSearchState.SOLVING)) {
            return problem.id.`in`(
                submits
                    .filter { it.state != ProblemSubmitState.ACCEPTED }
                    .map { it.problem.id }
            )
        }

        return null
    }

    private fun applyOrdering(query: JPAQuery<Problem>, order: ProblemSearchOrder) {
        when (order) {
            ProblemSearchOrder.LATEST -> {
                query.orderBy(problem._createdAt.desc())
            }

            ProblemSearchOrder.CORRECT_RATE_ASC -> {
                query.leftJoin(problem.submits, submit)
                    .groupBy(problem)
                    .orderBy(
                        submit.state.`when`(ProblemSubmitState.ACCEPTED)
                            .then(1L)
                            .otherwise(0L)
                            .sum()
                            .divide(submit.count())
                            .asc()
                            .nullsFirst()
                    )
            }

            ProblemSearchOrder.CORRECT_RATE_DESC -> {
                query.leftJoin(problem.submits, submit)
                    .groupBy(problem)
                    .orderBy(
                        submit.state.`when`(ProblemSubmitState.ACCEPTED)
                            .then(1L)
                            .otherwise(0L)
                            .sum()
                            .divide(submit.count())
                            .desc()
                            .nullsLast()
                    )
            }
        }
    }
}