package com.solve.domain.admin.problem.service.impl

import com.solve.domain.admin.problem.dto.request.AdminProblemCreateRequest
import com.solve.domain.admin.problem.dto.request.AdminProblemUpdateRequest
import com.solve.domain.admin.problem.dto.response.AdminProblemAuthorResponse
import com.solve.domain.admin.problem.dto.response.AdminProblemContributorResponse
import com.solve.domain.admin.problem.dto.response.AdminProblemResponse
import com.solve.domain.admin.problem.dto.response.AdminProblemTestCaseResponse
import com.solve.domain.admin.problem.service.AdminProblemService
import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemExample
import com.solve.domain.problem.domain.entity.ProblemTestCase
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.*
import com.solve.global.common.enums.Tier
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminProblemServiceImpl(
    private val securityHolder: SecurityHolder,
    private val problemRepository: ProblemRepository,
    private val problemTestCaseRepository: ProblemTestCaseRepository,
    private val problemExampleRepository: ProblemExampleRepository,
    private val problemContributorRepository: ProblemContributorRepository,
    private val problemSubmitRepository: ProblemSubmitRepository
) : AdminProblemService {
    @Transactional(readOnly = true)
    override fun getProblems(pageable: Pageable): Page<AdminProblemResponse> {
        return problemRepository.findAll(pageable).map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun getProblem(problemId: Long): AdminProblemResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        return problem.toResponse()
    }

    @Transactional
    override fun createProblem(request: AdminProblemCreateRequest) {
        val author = securityHolder.user
        val problem = Problem(
            title = request.title,
            content = request.content,
            input = request.input,
            output = request.output,
            tier = Tier.UNRATED,
            memoryLimit = request.memoryLimit,
            timeLimit = request.timeLimit,
            author = author
        )

        problemRepository.save(problem)

        for (testCase in request.testCases) {
            problemTestCaseRepository.save(
                ProblemTestCase(
                    input = testCase.input,
                    output = testCase.output,
                    problem = problem
                )
            )
        }

        for (example in request.examples) {
            problemExampleRepository.save(
                ProblemExample(
                    input = example.input,
                    output = example.output,
                    description = example.description,
                    problem = problem
                )
            )
        }
    }

    @Transactional
    override fun updateProblem(problemId: Long, request: AdminProblemUpdateRequest) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        request.title?.let { problem.title = it }
        request.content?.let { problem.content = it }
        request.input?.let { problem.input = it }
        request.output?.let { problem.output = it }
        request.memoryLimit?.let { problem.memoryLimit = it }
        request.timeLimit?.let { problem.timeLimit = it }

        problemRepository.save(problem)
    }

    @Transactional
    override fun deleteProblem(problemId: Long) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        problemRepository.delete(problem)
    }

    private fun Problem.toResponse() = AdminProblemResponse(
        id = id!!,
        title = title,
        content = content,
        input = input,
        output = output,
        memoryLimit = memoryLimit,
        timeLimit = timeLimit,
        testCases = problemTestCaseRepository.findAllByProblem(this).map { it.toResponse() },
        author = AdminProblemAuthorResponse.of(author),
        contributors = problemContributorRepository.findAllByProblem(this)
            .map { AdminProblemContributorResponse.of(it.user) },
        correctRate = problemSubmitRepository.findAllByProblem(this).let { submits ->
            (submits.map { it.state }
                .filter { it == ProblemSubmitState.ACCEPTED }.size.toDouble() / submits.size * 1000).toInt() / 10.0
        },
    )

    private fun ProblemTestCase.toResponse() = AdminProblemTestCaseResponse(
        input = input,
        output = output
    )
}