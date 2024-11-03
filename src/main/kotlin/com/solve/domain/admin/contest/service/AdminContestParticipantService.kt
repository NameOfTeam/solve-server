package com.solve.domain.admin.contest.service

import com.solve.domain.admin.contest.dto.request.AdminContestParticipantAddRequest
import java.util.*

interface AdminContestParticipantService {
    fun addContestParticipant(contestId: Long, request: AdminContestParticipantAddRequest)
    fun removeContestParticipant(contestId: Long, userId: UUID)
}