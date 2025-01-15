package com.solve.domain.admin.problem.service.impl

import com.solve.domain.admin.problem.dto.request.AdminProblemExampleCreateRequest
import com.solve.domain.admin.problem.dto.request.AdminProblemExampleUpdateRequest
import com.solve.domain.admin.problem.dto.response.AdminProblemExampleResponse
import com.solve.domain.admin.problem.service.AdminProblemExampleService
import com.solve.domain.problem.domain.entity.ProblemExample
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemExampleRepository
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.global.error.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminProblemExampleServiceImpl(
    private val problemRepository: ProblemRepository,
    private val problemExampleRepository: ProblemExampleRepository,
) : AdminProblemExampleService {
    @Transactional(readOnly = true)
    override fun getProblemExamples(problemId: Long): List<AdminProblemExampleResponse> {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val examples = problemExampleRepository.findAllByProblem(problem)

        return examples.map { AdminProblemExampleResponse.of(it) }
    }

    @Transactional
    override fun addProblemExample(problemId: Long, request: AdminProblemExampleCreateRequest) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        problemExampleRepository.save(
            ProblemExample(
                input = request.input,
                output = request.output,
                description = request.description,
                problem = problem
            )
        )
    }

    @Transactional
    override fun updateProblemExample(
        problemId: Long,
        exampleId: Long,
        request: AdminProblemExampleUpdateRequest
    ) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val example = problemExampleRepository.findByProblemAndId(problem, exampleId)
            ?: throw CustomException(ProblemError.TEST_CASE_NOT_FOUND)

        request.input?.let { example.input = it }
        request.output?.let { example.output = it }
        request.description?.let { example.description = it }
    }

    @Transactional
    override fun removeProblemExample(problemId: Long, exampleId: Long) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val example = problemExampleRepository.findByProblemAndId(problem, exampleId)
            ?: throw CustomException(ProblemError.TEST_CASE_NOT_FOUND)

        problemExampleRepository.delete(example)
    }
}