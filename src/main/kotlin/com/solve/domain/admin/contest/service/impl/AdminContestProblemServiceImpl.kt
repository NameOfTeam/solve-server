package com.solve.domain.admin.contest.service.impl

import com.solve.domain.admin.contest.dto.request.AdminContestProblemAddRequest
import com.solve.domain.admin.contest.service.AdminContestProblemService
import com.solve.domain.contest.domain.entity.ContestProblem
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.error.ContestProblemError
import com.solve.domain.contest.repository.ContestProblemRepository
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
    private val problemRepository: ProblemRepository,
    private val contestProblemRepository: ContestProblemRepository
) : AdminContestProblemService {
    @Transactional
    override fun addContestProblem(contestId: Long, request: AdminContestProblemAddRequest) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val problem =
            problemRepository.findByIdOrNull(request.problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        if (contestProblemRepository.existsByContestAndProblem(contest, problem))
            throw CustomException(ContestProblemError.CONTEST_PROBLEM_ALREADY_EXISTS)

        contestProblemRepository.save(ContestProblem(contest = contest, problem = problem, order = request.order, score = request.score))
    }

    @Transactional
    override fun removeContestProblem(contestId: Long, problemId: Long) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val contestProblem = contestProblemRepository.findByContestAndProblem(contest, problem)
            ?: throw CustomException(ContestProblemError.CONTEST_PROBLEM_NOT_FOUND)

        contestProblemRepository.delete(contestProblem)
    }
}