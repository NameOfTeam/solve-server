package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.enums.ProblemSearchOrder
import com.solve.domain.problem.domain.enums.ProblemSearchState
import com.solve.domain.problem.dto.response.ProblemResponse
import com.solve.domain.problem.repository.ProblemQueryRepository
import com.solve.domain.problem.service.ProblemSearchService
import com.solve.global.common.enums.Tier
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemSearchServiceImpl(
    private val problemQueryRepository: ProblemQueryRepository
) : ProblemSearchService {
    @Transactional(readOnly = true)
    override fun searchProblem(
        query: String,
        states: List<ProblemSearchState>,
        tiers: List<Tier>,
        order: ProblemSearchOrder,
        pageable: Pageable
    ): Page<ProblemResponse> {
        return problemQueryRepository.searchProblem(query, states, tiers, order, pageable)
            .map { ProblemResponse.of(it) }
    }
}