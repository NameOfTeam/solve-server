package com.solve.domain.problem.service.impl

import com.solve.domain.admin.problem.service.AdminProblemTestCaseService
import com.solve.domain.problem.domain.entity.ProblemTestCase
import com.solve.domain.problem.dto.request.ProblemTestCaseCreateRequest
import com.solve.domain.problem.dto.request.ProblemTestCaseUpdateRequest
import com.solve.domain.problem.dto.response.ProblemTestCaseResponse
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.global.error.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemTestCaseServiceImpl(
    private val problemRepository: ProblemRepository,
) : AdminProblemTestCaseService {
    @Transactional(readOnly = true)
    override fun getProblemTestCases(problemId: Long): List<ProblemTestCaseResponse> {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val testCases = problem.testCases

        return testCases.filter { it.sample }.map { ProblemTestCaseResponse.of(it) }
    }

    @Transactional
    override fun addProblemTestCase(problemId: Long, request: ProblemTestCaseCreateRequest) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        val testCase = ProblemTestCase(
            input = request.input,
            output = request.output,
            sample = request.sample,
            problem = problem
        )

        problem.testCases.add(testCase)
    }

    @Transactional
    override fun updateProblemTestCase(
        problemId: Long,
        testCaseId: Long,
        request: ProblemTestCaseUpdateRequest
    ): ProblemTestCaseResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val testCase = problem.testCases.find { it.id == testCaseId }
            ?: throw CustomException(ProblemError.TEST_CASE_NOT_FOUND)

        if (request.input != null) testCase.input = request.input
        if (request.output != null) testCase.output = request.output
        if (request.sample != null) testCase.sample = request.sample

        return ProblemTestCaseResponse.of(testCase)
    }

    @Transactional
    override fun removeProblemTestCase(problemId: Long, testCaseId: Long) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val testCase = problem.testCases.find { it.id == testCaseId }
            ?: throw CustomException(ProblemError.TEST_CASE_NOT_FOUND)

        problem.testCases.remove(testCase)
    }
}