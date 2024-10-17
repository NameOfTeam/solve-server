package com.solve.domain.contest.service

import com.solve.domain.contest.dto.request.ContestParticipantAddRequest
import java.util.*

interface ContestParticipantService {
    fun addContestParticipant(contestId: Long, request: ContestParticipantAddRequest)
    fun removeContestParticipant(contestId: Long, userId: UUID)
}