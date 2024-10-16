package com.solve.domain.admin.contest.service

import com.solve.domain.admin.contest.dto.request.AdminContestCreateRequest
import com.solve.domain.admin.contest.dto.request.AdminContestUpdateRequest
import com.solve.domain.admin.contest.dto.response.AdminContestResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface AdminContestService {
    fun getContests(pageable: Pageable): Page<AdminContestResponse>
    fun getContest(contestId: Long): AdminContestResponse
    fun createContest(request: AdminContestCreateRequest): AdminContestResponse
    fun updateContest(contestId: Long, request: AdminContestUpdateRequest): AdminContestResponse
    fun deleteContest(contestId: Long): AdminContestResponse
}