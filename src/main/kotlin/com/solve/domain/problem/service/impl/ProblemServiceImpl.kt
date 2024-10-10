package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.dto.request.ProblemCreateRequest
import com.solve.domain.problem.dto.request.ProblemUpdateRequest
import com.solve.domain.problem.dto.response.ProblemResponse
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
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
    private val problemRepository: ProblemRepository
) : ProblemService {
    @Transactional(readOnly = true)
    override fun getProblems(pageable: Pageable): Page<ProblemResponse> {
        return problemRepository.findAll(pageable).map { ProblemResponse.of(it) }
    }

    @Transactional(readOnly = true)
    override fun getProblem(problemId: Long): ProblemResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        return ProblemResponse.of(problem)
    }

    @Transactional
    override fun createProblem(request: ProblemCreateRequest): ProblemResponse {
        val author = securityHolder.user
        val problem = problemRepository.save(
            Problem(
                title = request.title,
                content = request.content,
                input = request.input,
                output = request.output,
                memoryLimit = request.memoryLimit,
                timeLimit = request.timeLimit,
                author = author
            )
        )

        return ProblemResponse.of(problem)
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