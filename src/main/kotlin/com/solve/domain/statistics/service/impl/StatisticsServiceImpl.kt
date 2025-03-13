package com.solve.domain.statistics.service.impl

import com.solve.domain.contest.domain.repository.ContestRepository
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.statistics.dto.response.StatisticsResponse
import com.solve.domain.statistics.service.StatisticsService
import com.solve.domain.user.repository.UserRepository
import com.solve.domain.workbook.repository.WorkbookRepository
import com.solve.global.common.enums.Tier
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class StatisticsServiceImpl(
    private val problemRepository: ProblemRepository,
    private val workbookRepository: WorkbookRepository,
    private val contestRepository: ContestRepository,
    private val userRepository: UserRepository
) : StatisticsService {
    @Transactional(readOnly = true)
    override fun getStatistics(): StatisticsResponse {
        val problemCount = problemRepository.count()
        val workbookCount = workbookRepository.count()
        val contestCount = contestRepository.count()
        val userCount = userRepository.countByTierIsNot(Tier.ROOKIE)

        return StatisticsResponse(
            problemCount,
            workbookCount,
            contestCount,
            userCount
        )
    }
}