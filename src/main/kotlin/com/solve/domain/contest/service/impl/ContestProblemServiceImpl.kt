package com.solve.domain.contest.service.impl

import com.solve.domain.contest.domain.entity.ContestProblem
import com.solve.domain.contest.dto.request.ContestProblemAddRequest
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.error.ContestOperatorError
import com.solve.domain.contest.error.ContestProblemError
import com.solve.domain.contest.repository.ContestRepository
import com.solve.domain.contest.service.ContestProblemService
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContestProblemServiceImpl(
    private val securityHolder: SecurityHolder,
    private val contestRepository: ContestRepository,
    private val problemRepository: ProblemRepository
) : ContestProblemService {
    @Transactional
    override fun addContestProblem(contestId: Long, request: ContestProblemAddRequest) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val problem =
            problemRepository.findByIdOrNull(request.problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val operator = securityHolder.user

        if (contest.operators.none { it.user == operator }) {
            throw CustomException(ContestOperatorError.CONTEST_OPERATOR_NOT_FOUND)
        }

        if (contest.problems.any { it.problem == problem }) {
            throw CustomException(ContestProblemError.CONTEST_PROBLEM_ALREADY_EXISTS)
        }

        contest.problems.add(ContestProblem(contest = contest, problem = problem))

        contestRepository.save(contest)
    }

    @Transactional
    override fun removeContestProblem(contestId: Long, problemId: Long) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        if (contest.operators.none { it.user == securityHolder.user }) {
            throw CustomException(ContestOperatorError.CONTEST_OPERATOR_NOT_FOUND)
        }

        if (contest.problems.none { it.problem == problem }) {
            throw CustomException(ContestProblemError.CONTEST_PROBLEM_NOT_FOUND)
        }

        contest.problems.removeIf { it.problem == problem }

        contestRepository.save(contest)
    }
}