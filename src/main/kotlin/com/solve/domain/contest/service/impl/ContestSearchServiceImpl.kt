package com.solve.domain.contest.service.impl

import com.solve.domain.contest.domain.enums.ContestSearchState
import com.solve.domain.contest.dto.response.ContestResponse
import com.solve.domain.contest.repository.ContestQueryRepository
import com.solve.domain.contest.service.ContestSearchService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContestSearchServiceImpl(
    private val contestQueryRepository: ContestQueryRepository
) : ContestSearchService {
    @Transactional(readOnly = true)
    override fun searchContest(query: String, state: ContestSearchState?, pageable: Pageable): Page<ContestResponse> {
        val contests = contestQueryRepository.searchContest(query, state, pageable)

        return contests.map { ContestResponse.of(it) }
    }
}