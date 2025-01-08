package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.enums.ProblemSearchOrder
import com.solve.domain.problem.domain.enums.ProblemSearchState
import com.solve.global.common.enums.Tier
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ProblemQueryRepository {
    fun searchProblem(
        query: String,
        states: List<ProblemSearchState>,
        tiers: List<Tier>,
        order: ProblemSearchOrder,
        pageable: Pageable
    ): Page<Problem>
}