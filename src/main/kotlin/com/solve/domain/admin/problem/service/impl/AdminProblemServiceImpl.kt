package com.solve.domain.admin.problem.service.impl

import com.solve.domain.admin.problem.dto.request.AdminProblemCreateRequest
import com.solve.domain.admin.problem.dto.response.AdminProblemResponse
import com.solve.domain.admin.problem.service.AdminProblemService
import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.dto.response.ProblemResponse
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
    override fun createProblem(request: AdminProblemCreateRequest): ProblemResponse {
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
}