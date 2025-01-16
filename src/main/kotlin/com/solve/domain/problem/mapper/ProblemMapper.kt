package com.solve.domain.problem.mapper

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.dto.response.ProblemAuthorResponse
import com.solve.domain.problem.dto.response.ProblemExampleResponse
import com.solve.domain.problem.dto.response.ProblemResponse
import com.solve.domain.problem.repository.ProblemExampleRepository
import com.solve.domain.problem.repository.ProblemSubmitRepository
import com.solve.global.security.holder.SecurityHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ProblemMapper(
    private val problemSubmitRepository: ProblemSubmitRepository,
    private val problemExampleRepository: ProblemExampleRepository,
    private val securityHolder: SecurityHolder
) {
    @Transactional(readOnly = true)
    fun toResponse(problem: Problem): ProblemResponse {
        val submits = problemSubmitRepository.findAllByProblem(problem)
        val acceptedSubmits = submits.filter { it.state == ProblemSubmitState.ACCEPTED }
        val distinctSolvedCount = acceptedSubmits.distinctBy { it.author }.size
        val correctRate = if (submits.isNotEmpty()) {
            (acceptedSubmits.size.toDouble() / submits.size * 1000).toInt() / 10.0
        } else 0.0

        return ProblemResponse(
            id = problem.id!!,
            title = problem.title,
            content = problem.content,
            input = problem.input,
            output = problem.output,
            memoryLimit = problem.memoryLimit,
            timeLimit = problem.timeLimit,
            tier = problem.tier,
            tags = problem.tags,
            solvedCount = distinctSolvedCount,
            examples = problemExampleRepository
                .findAllByProblem(problem).map { ProblemExampleResponse.of(it) },
            author = ProblemAuthorResponse.of(problem.author),
            correctRate = correctRate
        ).apply {
            if (!securityHolder.isAuthenticated) return@apply

            val mySubmits = problemSubmitRepository.findAllByProblemAndAuthor(problem, securityHolder.user)

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