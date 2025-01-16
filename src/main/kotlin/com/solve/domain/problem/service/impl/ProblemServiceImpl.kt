package com.solve.domain.problem.service.impl

import com.solve.domain.problem.dto.response.ProblemResponse
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.mapper.ProblemMapper
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.service.ProblemService
import com.solve.global.error.CustomException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemServiceImpl(
    private val problemRepository: ProblemRepository,
    private val problemMapper: ProblemMapper
) : ProblemService {
    @Transactional(readOnly = true)
    override fun getProblems(pageable: Pageable): Page<ProblemResponse> {
        val problems = problemRepository.findAll(pageable)

        return problems.map { problemMapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun getProblem(problemId: Long): ProblemResponse {
        val problem = problemRepository.findByIdOrNull(problemId)
            ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND, problemId)

        return problemMapper.toResponse(problem)
    }
}