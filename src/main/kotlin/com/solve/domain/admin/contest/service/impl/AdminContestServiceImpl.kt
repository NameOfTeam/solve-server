package com.solve.domain.admin.contest.service.impl

import com.solve.domain.admin.contest.dto.request.AdminContestCreateRequest
import com.solve.domain.admin.contest.dto.request.AdminContestUpdateRequest
import com.solve.domain.admin.contest.dto.response.AdminContestResponse
import com.solve.domain.admin.contest.service.AdminContestService
import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.entity.ContestOperator
import com.solve.domain.contest.domain.entity.ContestParticipant
import com.solve.domain.contest.domain.entity.ContestProblem
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.repository.ContestRepository
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminContestServiceImpl(
    private val securityHolder: SecurityHolder,
    private val userRepository: UserRepository,
    private val problemRepository: ProblemRepository,
    private val contestRepository: ContestRepository
) : AdminContestService {
    @Transactional(readOnly = true)
    override fun getContests(pageable: Pageable): Page<AdminContestResponse> {
        return contestRepository.findAll(pageable).map { AdminContestResponse.of(it) }
    }

    @Transactional(readOnly = true)
    override fun getContest(contestId: Long): AdminContestResponse {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        return AdminContestResponse.of(contest)
    }

    @Transactional
    override fun createContest(request: AdminContestCreateRequest): AdminContestResponse {
        val operators = userRepository.findAllById(request.operatorIds)
        val participants = userRepository.findAllById(request.participantIds)
        val problems = problemRepository.findAllById(request.problemIds)
        val owner = securityHolder.user

        var contest = Contest(
            title = request.title,
            description = request.description,
            startAt = request.startAt,
            endAt = request.endAt,
            owner = owner
        )

        contest.operators.addAll(operators.map {
            ContestOperator(
                contest = contest,
                user = it
            )
        })
        contest.participants.addAll(participants.map {
            ContestParticipant(
                contest = contest,
                user = it
            )
        })
        contest.problems.addAll(problems.map {
            ContestProblem(
                contest = contest,
                problem = it
            )
        })

        contest = contestRepository.save(contest)

        return AdminContestResponse.of(contest)
    }

    @Transactional
    override fun updateContest(contestId: Long, request: AdminContestUpdateRequest): AdminContestResponse {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        request.title?.let { contest.title = it }
        request.description?.let { contest.description = it }
        request.startAt?.let { contest.startAt = it }
        request.endAt?.let { contest.endAt = it }

        return AdminContestResponse.of(contest)
    }

    @Transactional
    override fun deleteContest(contestId: Long): AdminContestResponse {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        contestRepository.delete(contest)

        return AdminContestResponse.of(contest)
    }
}