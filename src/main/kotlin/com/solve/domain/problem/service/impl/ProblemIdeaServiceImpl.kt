package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.ProblemIdea
import com.solve.domain.problem.dto.request.ProblemIdeaCreateRequest
import com.solve.domain.problem.dto.request.ProblemIdeaUpdateRequest
import com.solve.domain.problem.dto.response.ProblemIdeaResponse
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.error.ProblemIdeaError
import com.solve.domain.problem.repository.ProblemIdeaRepository
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.service.ProblemIdeaService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemIdeaServiceImpl(
    private val securityHolder: SecurityHolder,
    private val problemRepository: ProblemRepository,
    private val problemIdeaRepository: ProblemIdeaRepository
) : ProblemIdeaService {
    @Transactional(readOnly = true)
    override fun getProblemIdeas(problemId: Long): List<ProblemIdeaResponse> {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        val ideas = problemIdeaRepository.findAllByProblem(problem)

        return ideas.map { ProblemIdeaResponse.ofMe(it) }
    }

    @Transactional(readOnly = true)
    override fun getProblemIdea(problemId: Long, ideaId: Long): ProblemIdeaResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val idea = problemIdeaRepository.findByIdAndProblem(ideaId, problem)
            ?: throw CustomException(ProblemIdeaError.PROBLEM_IDEA_NOT_FOUND)

        return ProblemIdeaResponse.ofMe(idea)
    }

    @Transactional
    override fun createProblemIdea(problemId: Long, request: ProblemIdeaCreateRequest): ProblemIdeaResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val author = securityHolder.user

        val idea = problemIdeaRepository.save(
            ProblemIdea(
                problem = problem,
                author = author,
                title = request.title,
                content = request.content
            )
        )

        return ProblemIdeaResponse.ofMe(idea)
    }

    @Transactional
    override fun updateProblemIdea(
        problemId: Long,
        ideaId: Long,
        request: ProblemIdeaUpdateRequest
    ): ProblemIdeaResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        var idea = problemIdeaRepository.findByIdAndProblem(ideaId, problem)
            ?: throw CustomException(ProblemIdeaError.PROBLEM_IDEA_NOT_FOUND)

        request.content?.let { idea.content = it }

        idea = problemIdeaRepository.save(idea)

        return ProblemIdeaResponse.ofMe(idea)
    }

    @Transactional
    override fun deleteProblemIdea(problemId: Long, ideaId: Long): ProblemIdeaResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val idea = problemIdeaRepository.findByIdAndProblem(ideaId, problem)
            ?: throw CustomException(ProblemIdeaError.PROBLEM_IDEA_NOT_FOUND)

        problemIdeaRepository.delete(idea)

        return ProblemIdeaResponse.ofMe(idea)
    }

    private fun ProblemIdeaResponse.Companion.ofMe(idea: ProblemIdea) = of(idea).apply {
        if (!securityHolder.isAuthenticated) {
            return this
        }

        val me = securityHolder.user

        isAuthor = idea.author == me
        isLiked = idea.likes.any { it.author == me }
    }
}