package com.solve.domain.submit.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.submit.domain.entity.QSubmit
import com.solve.domain.submit.domain.entity.Submit
import com.solve.domain.submit.domain.enums.SubmitState
import com.solve.domain.user.domain.entity.User
import com.solve.global.common.enums.ProgrammingLanguage
import com.solve.global.security.holder.SecurityHolder
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class SubmitQueryRepository(
    private val securityHolder: SecurityHolder,
    private val queryFactory: JPAQueryFactory
) {
    private val submit = QSubmit.submit

    @Transactional(readOnly = true)
    fun getMySubmits(problem: Problem?, cursor: Submit?, size: Int): List<Submit> {
        val me = securityHolder.user

        return queryFactory
            .selectFrom(submit)
            .where(
                problemEq(problem),
                authorEq(me),
                cursorLt(cursor)
            )
            .orderBy(submit._createdAt.desc(), submit.id.desc())
            .limit(size.toLong())
            .fetch() ?: emptyList()
    }

    @Transactional(readOnly = true)
    fun searchSubmit(
        problem: Problem?,
        author: User?,
        state: SubmitState?,
        language: ProgrammingLanguage?,
        cursor: Submit?,
        size: Int
    ): List<Submit> {
        return queryFactory
            .selectFrom(submit)
            .where(
                problemEq(problem),
                authorEq(author),
                stateEq(state),
                languageEq(language),
                cursorLt(cursor)
            )
            .orderBy(submit._createdAt.desc(), submit.id.desc())
            .limit(size.toLong())
            .fetch() ?: emptyList()
    }

    private fun problemEq(problem: Problem?) = problem?.let { submit.problem.eq(it) }
    private fun authorEq(user: User?) = user?.let { submit.author.eq(it) }
    private fun stateEq(state: SubmitState?) = state?.let { submit.state.eq(it) }
    private fun languageEq(language: ProgrammingLanguage?) = language?.let { submit.language.eq(it) }
    private fun cursorLt(cursor: Submit?) = cursor?.id?.let { submit.id.lt(it) }
}