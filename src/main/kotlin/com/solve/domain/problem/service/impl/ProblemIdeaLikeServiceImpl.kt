package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.ProblemIdeaLike
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.error.ProblemIdeaError
import com.solve.domain.problem.repository.ProblemIdeaRepository
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.service.ProblemIdeaLikeService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemIdeaLikeServiceImpl(
    private val securityHolder: SecurityHolder,
    private val problemRepository: ProblemRepository,
    private val problemIdeaRepository: ProblemIdeaRepository
) : ProblemIdeaLikeService {
    @Transactional
    override fun toggleProblemIdeaLike(problemId: Long, ideaId: Long) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val idea = problemIdeaRepository.findByIdAndProblem(ideaId, problem)
            ?: throw CustomException(ProblemIdeaError.PROBLEM_IDEA_NOT_FOUND)

        val user = securityHolder.user

        if (idea.likes.any { it.author == user }) {
            idea.removeLike(user)
        } else {
            idea.addLike(ProblemIdeaLike(author = user, idea = idea))
        }

        problemIdeaRepository.save(idea)
    }
}