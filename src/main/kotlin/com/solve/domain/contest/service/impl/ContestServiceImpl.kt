package com.solve.domain.contest.service.impl

import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.enums.ContestState
import com.solve.domain.contest.dto.response.*
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.repository.ContestOperatorRepository
import com.solve.domain.contest.repository.ContestRepository
import com.solve.domain.contest.service.ContestService
import com.solve.global.error.CustomException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ContestServiceImpl(
    private val contestRepository: ContestRepository,
    private val contestOperatorRepository: ContestOperatorRepository
) : ContestService {
    @Transactional(readOnly = true)
    override fun getContests(pageable: Pageable): Page<ContestResponse> {
        return contestRepository.findAll(pageable).map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun getContest(contestId: Long): ContestResponse {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        return contest.toResponse()
    }

    private fun Contest.toResponse() = ContestResponse(
        id = id!!,
        title = title,
        description = description,
        startAt = startAt,
        endAt = endAt,
        owner = ContestOwnerResponse.of(owner),
        visibility = visibility,
        state = if (startAt.isAfter(LocalDateTime.now())) {
            ContestState.UPCOMING
        } else if (endAt.isBefore(LocalDateTime.now())) {
            ContestState.ENDED
        } else {
            ContestState.ONGOING
        },
        winner = winner?.let { ContestWinnerResponse.of(it) },
        operators = contestOperatorRepository.findAllByContest(this).map { ContestOperatorResponse.of(it) },
        participants = participants.map { ContestParticipantResponse.of(it) },
        problems = problems.map { ContestProblemResponse.of(it) },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}