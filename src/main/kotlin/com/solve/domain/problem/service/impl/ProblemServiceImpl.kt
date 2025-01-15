package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.dto.response.ProblemAuthorResponse
import com.solve.domain.problem.dto.response.ProblemExampleResponse
import com.solve.domain.problem.dto.response.ProblemResponse
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemExampleRepository
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
    private val problemSubmitRepository: ProblemSubmitRepository,
    private val problemExampleRepository: ProblemExampleRepository
) : ProblemService {
    @Transactional(readOnly = true)
    override fun getProblems(pageable: Pageable): Page<ProblemResponse> {
        val problems = problemRepository.findAll(pageable)

        return problems.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun getProblem(problemId: Long): ProblemResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND, problemId)

        return problem.toResponse()
    }

    fun Problem.toResponse() = ProblemResponse(
        id = id!!,
        title = title,
        content = content,
        input = input,
        output = output,
        memoryLimit = memoryLimit,
        timeLimit = timeLimit,
        tier = tier,
        solvedCount = submits.filter { it.state == ProblemSubmitState.ACCEPTED }.distinctBy { it.author }
            .count(),
        examples = problemExampleRepository.findAllByProblem(this).map { ProblemExampleResponse.of(it) },
        author = ProblemAuthorResponse.of(author),
        correctRate = (submits.map { it.state }
            .filter { it == ProblemSubmitState.ACCEPTED }.size.toDouble() / submits.size * 1000).toInt() / 10.0,
    ).apply {
        if (!securityHolder.isAuthenticated) return@apply

        val me = securityHolder.user
        val submits = problemSubmitRepository.findAllByProblemAndAuthor(this@toResponse, me)

        if (submits.any { it.state == ProblemSubmitState.ACCEPTED }) state = ProblemSubmitState.ACCEPTED
        else if (submits.any { it.state == ProblemSubmitState.WRONG_ANSWER || it.state == ProblemSubmitState.RUNTIME_ERROR || it.state == ProblemSubmitState.TIME_LIMIT_EXCEEDED || it.state == ProblemSubmitState.MEMORY_LIMIT_EXCEEDED || it.state == ProblemSubmitState.TIME_LIMIT_EXCEEDED || it.state == ProblemSubmitState.COMPILE_ERROR }) state =
            ProblemSubmitState.WRONG_ANSWER
    }
}