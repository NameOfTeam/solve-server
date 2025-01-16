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
        startAt = contest.startAt,
        endAt = contest.endAt,
        owner = ContestOwnerResponse.of(contest.owner),
        visibility = contest.visibility,
        state = if (contest.startAt.isAfter(LocalDateTime.now())) {
            ContestState.UPCOMING
        } else if (contest.endAt.isBefore(LocalDateTime.now())) {
            ContestState.ENDED
        } else {
            ContestState.ONGOING
        },
        winner = contest.winner?.let { ContestWinnerResponse.of(it) },
        operators = contestOperatorRepository.findAllByContest(contest).map { ContestOperatorResponse.of(it) },
        participants = contestParticipantRepository.findAllByContest(contest).map { ContestParticipantResponse.of(it) },
        problems = contestProblemRepository.findAllByContest(contest).map { ContestProblemResponse.of(it) },
        createdAt = contest.createdAt,
        updatedAt = contest.updatedAt
    )
}