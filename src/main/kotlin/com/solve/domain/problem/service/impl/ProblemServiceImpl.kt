package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.dto.request.ProblemUpdateRequest
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

        if (securityHolder.isAuthenticated) {
            val user = securityHolder.user

            return problems.map {
                val submits = problemSubmitRepository.findAllByProblemAndAuthor(it, user)
                var state: ProblemSubmitState? = null

                if (submits.any { it.state == ProblemSubmitState.ACCEPTED }) {
                    state = ProblemSubmitState.ACCEPTED
                } else if (submits.any { it.state == ProblemSubmitState.WRONG_ANSWER || it.state == ProblemSubmitState.RUNTIME_ERROR || it.state == ProblemSubmitState.TIME_LIMIT_EXCEEDED || it.state == ProblemSubmitState.MEMORY_LIMIT_EXCEEDED || it.state == ProblemSubmitState.TIME_LIMIT_EXCEEDED || it.state == ProblemSubmitState.COMPILE_ERROR }) {
                    state = ProblemSubmitState.WRONG_ANSWER
                }

                val response = ProblemResponse.of(it, state)

                response.correctRate(submits)
                response
            }
        } else {
            return problems.map { ProblemResponse.of(it) }
        }
    }

    @Transactional(readOnly = true)
    override fun getProblem(problemId: Long): ProblemResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        if (securityHolder.isAuthenticated) {
            val user = securityHolder.user
            val submits = problemSubmitRepository.findAllByProblemAndAuthor(problem, user)
            val state: ProblemSubmitState

            if (submits.any { it.state == ProblemSubmitState.ACCEPTED }) {
                state = ProblemSubmitState.ACCEPTED
            } else {
                state = ProblemSubmitState.WRONG_ANSWER
            }

            val response = ProblemResponse.of(problem, state)
            response.correctRate(submits)

            return response
        } else {
            return ProblemResponse.of(problem)
        }
    }

    @Transactional
    override fun updateProblem(problemId: Long, request: ProblemUpdateRequest): ProblemResponse {
        val user = securityHolder.user
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        if (problem.author != user) throw CustomException(ProblemError.PROBLEM_NOT_AUTHORIZED)

        if (request.title != null) problem.title = request.title
        if (request.content != null) problem.content = request.content
        if (request.input != null) problem.input = request.input
        if (request.output != null) problem.output = request.output
        if (request.memoryLimit != null) problem.memoryLimit = request.memoryLimit
        if (request.timeLimit != null) problem.timeLimit = request.timeLimit

        return ProblemResponse.of(problem)
    }

    @Transactional
    override fun deleteProblem(problemId: Long) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        problemRepository.delete(problem)
    }
}