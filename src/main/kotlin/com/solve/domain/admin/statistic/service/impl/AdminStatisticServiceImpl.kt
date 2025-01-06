package com.solve.domain.admin.statistic.service.impl

import com.solve.domain.admin.statistic.service.AdminStatisticService
import com.solve.domain.contest.repository.ContestRepository
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.repository.ProblemSubmitRepository
import com.solve.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AdminStatisticServiceImpl(
    private val problemRepository: ProblemRepository,
    private val userRepository: UserRepository,
    private val contestRepository: ContestRepository,
    private val problemSubmitRepository: ProblemSubmitRepository
) : AdminStatisticService {
    override fun getTotalUsers() = userRepository.count()

    override fun getTotalContests() = contestRepository.count()

    override fun getTotalProblems() = problemRepository.count()

    override fun getTotalSubmissions() = problemSubmitRepository.count()

    override fun getTotalOngoingContests() = LocalDateTime.now().let {
        contestRepository.countByEndAtGreaterThanEqualAndStartAtLessThanEqual(it, it)
    }
}