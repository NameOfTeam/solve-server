package com.solve.domain.contest.mapper

import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.enums.ContestState
import com.solve.domain.contest.dto.response.*
import com.solve.domain.contest.repository.ContestOperatorRepository
import com.solve.domain.contest.repository.ContestParticipantRepository
import com.solve.domain.contest.repository.ContestProblemRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ContestMapper(
    private val contestOperatorRepository: ContestOperatorRepository,
    private val contestParticipantRepository: ContestParticipantRepository,
    private val contestProblemRepository: ContestProblemRepository
) {
    fun toResponse(contest: Contest) = ContestResponse(
        id = contest.id!!,
        title = contest.title,
        description = contest.description,
        startTime = contest.startTime,
        endTime = contest.endTime,
        owner = ContestOwnerResponse.of(contest.owner),
        state = when {
            contest.startTime.isAfter(LocalDateTime.now()) -> ContestState.UPCOMING
            contest.endTime.isBefore(LocalDateTime.now()) -> ContestState.ENDED
            else -> ContestState.ONGOING
        },
        winner = contest.winner?.let { ContestWinnerResponse.of(it) },
        operators = contestOperatorRepository.findAllByContest(contest).map { ContestOperatorResponse.of(it) },
        participants = contestParticipantRepository.findAllByContest(contest).map { ContestParticipantResponse.of(it) },
        problems = if (contest.startTime.isBefore(LocalDateTime.now())) {
            contestProblemRepository.findAllByContest(contest).map { ContestProblemResponse.of(it) }
        } else {
            emptyList()
        },
        createdAt = contest.createdAt,
        updatedAt = contest.updatedAt
    )
}