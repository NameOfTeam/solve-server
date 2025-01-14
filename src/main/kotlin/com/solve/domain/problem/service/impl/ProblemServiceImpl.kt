package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.dto.response.ProblemResponse
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.repository.ProblemSubmitRepository
import com.solve.domain.problem.service.ProblemService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemServiceImpl(
    private val securityHolder: SecurityHolder,
    private val problemRepository: ProblemRepository,
    private val problemSubmitRepository: ProblemSubmitRepository
) : ProblemService {
    @Transactional(readOnly = true)
    override fun getProblems(pageable: Pageable): Page<ProblemResponse> {
        val problems = problemRepository.findAll(pageable)

        return problems.map { ProblemResponse.ofMe(it) }
    }

    @Transactional(readOnly = true)
    override fun getProblem(problemId: Long): ProblemResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND, problemId)

        return ProblemResponse.ofMe(problem)
    }

    private fun ProblemResponse.Companion.ofMe(problem: Problem) = of(problem).apply {
        if (!securityHolder.isAuthenticated) {
            return this
        }

        val me = securityHolder.user
        val submits = problemSubmitRepository.findAllByProblemAndAuthor(problem, me)

        if (submits.any { it.state == ProblemSubmitState.ACCEPTED }) state = ProblemSubmitState.ACCEPTED
        else if (submits.any { it.state == ProblemSubmitState.WRONG_ANSWER || it.state == ProblemSubmitState.RUNTIME_ERROR || it.state == ProblemSubmitState.TIME_LIMIT_EXCEEDED || it.state == ProblemSubmitState.MEMORY_LIMIT_EXCEEDED || it.state == ProblemSubmitState.TIME_LIMIT_EXCEEDED || it.state == ProblemSubmitState.COMPILE_ERROR }) state =
            ProblemSubmitState.WRONG_ANSWER
    }
}