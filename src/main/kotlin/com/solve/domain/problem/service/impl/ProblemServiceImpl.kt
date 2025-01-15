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
        val problem = problemRepository.findByIdOrNull(problemId)
            ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND, problemId)

        return problem.toResponse()
    }

    fun Problem.toResponse(): ProblemResponse {
        val submits = problemSubmitRepository.findAllByProblem(this)
        val acceptedSubmits = submits.filter { it.state == ProblemSubmitState.ACCEPTED }
        val distinctSolvedCount = acceptedSubmits.distinctBy { it.author }.size
        val correctRate = if (submits.isNotEmpty()) {
            (acceptedSubmits.size.toDouble() / submits.size * 1000).toInt() / 10.0
        } else 0.0

        return ProblemResponse(
            id = id!!,
            title = title,
            content = content,
            input = input,
            output = output,
            memoryLimit = memoryLimit,
            timeLimit = timeLimit,
            tier = tier,
            solvedCount = distinctSolvedCount,
            examples = problemExampleRepository.findAllByProblem(this).map { ProblemExampleResponse.of(it) },
            author = ProblemAuthorResponse.of(author),
            correctRate = correctRate
        ).apply {
            if (!securityHolder.isAuthenticated) return@apply

            val mySubmits = problemSubmitRepository.findAllByProblemAndAuthor(this@toResponse, securityHolder.user)

            if (mySubmits.any { it.state == ProblemSubmitState.ACCEPTED }) {
                state = ProblemSubmitState.ACCEPTED
            } else if (mySubmits.any {
                    it.state in listOf(
                        ProblemSubmitState.WRONG_ANSWER,
                        ProblemSubmitState.RUNTIME_ERROR,
                        ProblemSubmitState.TIME_LIMIT_EXCEEDED,
                        ProblemSubmitState.MEMORY_LIMIT_EXCEEDED,
                        ProblemSubmitState.COMPILE_ERROR
                    )
                }) {
                state = ProblemSubmitState.WRONG_ANSWER
            }
        }
    }
}