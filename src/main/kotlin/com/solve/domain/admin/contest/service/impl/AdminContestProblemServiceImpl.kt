package com.solve.domain.admin.contest.service.impl

import com.solve.domain.admin.contest.dto.request.AdminContestProblemAddRequest
import com.solve.domain.admin.contest.dto.response.*
import com.solve.domain.admin.contest.service.AdminContestProblemService
import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.entity.ContestProblem
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.error.ContestProblemError
import com.solve.domain.contest.repository.ContestOperatorRepository
import com.solve.domain.contest.repository.ContestRepository
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.global.error.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminContestProblemServiceImpl(
    private val contestRepository: ContestRepository,
    private val problemRepository: ProblemRepository
) : AdminContestProblemService {
    @Transactional
    override fun addContestProblem(contestId: Long, request: AdminContestProblemAddRequest) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val problem =
            problemRepository.findByIdOrNull(request.problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        if (contest.problems.any { it.problem == problem }) {
            throw CustomException(ContestProblemError.CONTEST_PROBLEM_ALREADY_EXISTS)
        }

        contest.problems.add(ContestProblem(contest = contest, problem = problem))
    }

    @Transactional
    override fun removeContestProblem(contestId: Long, problemId: Long) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val problem = contest.problems.find { it.problem.id == problemId }
            ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        contest.problems.remove(problem)
    }
}