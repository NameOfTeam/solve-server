package com.solve.domain.contest.domain.repository

import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.enums.ContestSearchState
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ContestQueryRepository {
    fun searchContest(query: String, state: ContestSearchState?, pageable: Pageable): Page<Contest>
}