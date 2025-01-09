package com.solve.domain.admin.problem.service.impl

import com.solve.domain.admin.problem.dto.request.AdminProblemCreateRequest
import com.solve.domain.admin.problem.dto.request.AdminProblemUpdateRequest
import com.solve.domain.admin.problem.dto.response.AdminProblemResponse
import com.solve.domain.admin.problem.service.AdminProblemService
import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemTestCase
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminProblemServiceImpl(
    private val securityHolder: SecurityHolder,
    private val problemRepository: ProblemRepository
) : AdminProblemService {
    @Transactional(readOnly = true)
    override fun getProblems(pageable: Pageable): Slice<AdminProblemResponse> {
        return problemRepository.findAll(pageable).map { AdminProblemResponse.of(it) }
    }

    @Transactional(readOnly = true)
    override fun getProblem(problemId: Long): AdminProblemResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        return AdminProblemResponse.of(problem)
    }

    @Transactional
    override fun createProblem(request: AdminProblemCreateRequest): AdminProblemResponse {
        val author = securityHolder.user
        var problem = problemRepository.save(
            Problem(
                title = request.title,
                content = request.content,
                input = request.input,
                output = request.output,
                tier = request.tier,
                memoryLimit = request.memoryLimit,
                timeLimit = request.timeLimit,
                author = author
            )
        )

        request.testCases?.forEach {
            problem.testCases.add(
                ProblemTestCase(
                    input = it.input,
                    output = it.output,
                    sample = it.sample,
                    problem = problem
                )
            )
        }

        problem = problemRepository.save(problem)

        return AdminProblemResponse.of(problem)
    }

    @Transactional
    override fun updateProblem(problemId: Long, request: AdminProblemUpdateRequest): AdminProblemResponse {
        var problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        request.title?.let { problem.title = it }
        request.content?.let { problem.content = it }
        request.input?.let { problem.input = it }
        request.output?.let { problem.output = it }
        request.memoryLimit?.let { problem.memoryLimit = it }
        request.timeLimit?.let { problem.timeLimit = it }

        problem = problemRepository.save(problem)

        return AdminProblemResponse.of(problem)
    }

    @Transactional
    override fun deleteProblem(problemId: Long) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        problemRepository.delete(problem)
    }
}