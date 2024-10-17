package com.solve.domain.admin.contest.service

import com.solve.domain.admin.contest.dto.request.AdminContestOperatorAddRequest
import java.util.*

interface AdminContestOperatorService {
    fun addContestOperator(contestId: Long, request: AdminContestOperatorAddRequest)
    fun removeContestOperator(contestId: Long, userId: UUID)
}