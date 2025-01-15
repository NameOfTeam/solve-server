package com.solve.domain.contest.service.impl

import com.solve.domain.admin.contest.dto.response.*
import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.enums.ContestSearchState
import com.solve.domain.contest.domain.enums.ContestState
import com.solve.domain.contest.dto.response.*
import com.solve.domain.contest.repository.ContestOperatorRepository
import com.solve.domain.contest.repository.ContestQueryRepository
import com.solve.domain.contest.service.ContestSearchService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ContestSearchServiceImpl(
    private val contestQueryRepository: ContestQueryRepository,
    private val contestOperatorRepository: ContestOperatorRepository
) : ContestSearchService {
    @Transactional(readOnly = true)
    override fun searchContest(query: String, state: ContestSearchState?, pageable: Pageable): Page<ContestResponse> {
        val contests = contestQueryRepository.searchContest(query, state, pageable)

        return contests.map { it.toResponse() }
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