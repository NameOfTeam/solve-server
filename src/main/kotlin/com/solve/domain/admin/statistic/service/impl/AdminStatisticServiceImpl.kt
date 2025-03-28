package com.solve.domain.admin.statistic.service.impl

import com.solve.domain.admin.statistic.service.AdminStatisticService
import com.solve.domain.contest.domain.repository.ContestRepository
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.submit.repository.SubmitRepository
import com.solve.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class AdminStatisticServiceImpl(
    private val problemRepository: ProblemRepository,
    private val userRepository: UserRepository,
    private val contestRepository: ContestRepository,
    private val submitRepository: SubmitRepository
) : AdminStatisticService {
    override fun getTotalUsers() = userRepository.count()

    override fun getTotalContests() = contestRepository.count()

    override fun getTotalProblems() = problemRepository.count()

    override fun getTotalSubmissions() = submitRepository.count()

    override fun getTotalOngoingContests() = LocalDateTime.now().let {
        contestRepository.countByEndTimeGreaterThanEqualAndStartTimeLessThanEqual(it, it)
    }
}