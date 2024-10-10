package com.solve.domain.contest.service

import com.solve.domain.contest.dto.response.ContestResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ContestService {
    fun getContests(pageable: Pageable): Page<ContestResponse>
    fun getContest(contestId: Long): ContestResponse
}