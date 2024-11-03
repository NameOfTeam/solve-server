package com.solve.domain.contest.service

interface ContestParticipantService {
    fun joinContest(contestId: Long)
    fun leaveContest(contestId: Long)
}