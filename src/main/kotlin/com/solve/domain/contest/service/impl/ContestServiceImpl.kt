package com.solve.domain.contest.service.impl

import com.solve.domain.contest.dto.response.ContestResponse
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.repository.ContestRepository
import com.solve.domain.contest.service.ContestService
import com.solve.global.error.CustomException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContestServiceImpl(
    private val contestRepository: ContestRepository
) : ContestService {
    @Transactional(readOnly = true)
    override fun getContests(pageable: Pageable): Page<ContestResponse> {
        return contestRepository.findAll(pageable).map { ContestResponse.of(it) }
    }

    @Transactional(readOnly = true)
    override fun getContest(contestId: Long): ContestResponse {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        return ContestResponse.of(contest)
    }
}