package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.enums.ProblemSearchOrder
import com.solve.domain.problem.domain.enums.ProblemSearchState
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.dto.response.ProblemAuthorResponse
import com.solve.domain.problem.dto.response.ProblemExampleResponse
import com.solve.domain.problem.dto.response.ProblemResponse
import com.solve.domain.problem.repository.ProblemExampleRepository
import com.solve.domain.problem.repository.ProblemQueryRepository
import com.solve.domain.problem.repository.ProblemSubmitRepository
import com.solve.domain.problem.service.ProblemSearchService
import com.solve.global.common.enums.Tier
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemSearchServiceImpl(
    private val problemQueryRepository: ProblemQueryRepository,
    private val problemExampleRepository: ProblemExampleRepository,
    private val securityHolder: SecurityHolder,
    private val problemSubmitRepository: ProblemSubmitRepository,
) : ProblemSearchService {
    @Transactional(readOnly = true)
    override fun searchProblem(
        query: String,
        states: List<ProblemSearchState>,
        tiers: List<Tier>,
        order: ProblemSearchOrder,
        pageable: Pageable
    ): Page<ProblemResponse> {
        return problemQueryRepository.searchProblem(query, states, tiers, order, pageable)
            .map { it.toResponse() }
    }

    private fun Problem.toResponse() = ProblemResponse(
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