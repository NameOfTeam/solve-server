package com.solve.domain.contest.service

import com.solve.domain.contest.domain.enums.ContestSearchState
import com.solve.domain.contest.dto.response.ContestResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ContestSearchService {
    fun searchContest(query: String, state: ContestSearchState?, pageable: Pageable): Page<ContestResponse>
}