package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.ProblemIdeaComment
import com.solve.domain.problem.dto.request.ProblemIdeaCommentCreateRequest
import com.solve.domain.problem.dto.request.ProblemIdeaCommentUpdateRequest
import com.solve.domain.problem.dto.response.ProblemIdeaCommentResponse
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.error.ProblemIdeaCommentError
import com.solve.domain.problem.error.ProblemIdeaError
import com.solve.domain.problem.repository.ProblemIdeaCommentRepository
import com.solve.domain.problem.repository.ProblemIdeaRepository
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.service.ProblemIdeaCommentService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemIdeaCommentServiceImpl(
    private val securityHolder: SecurityHolder,
    private val problemRepository: ProblemRepository,
    private val problemIdeaRepository: ProblemIdeaRepository,
    private val problemIdeaCommentRepository: ProblemIdeaCommentRepository
) : ProblemIdeaCommentService {
    @Transactional(readOnly = true)
    override fun getProblemIdeaComments(problemId: Long, ideaId: Long): List<ProblemIdeaCommentResponse> {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val idea = problemIdeaRepository.findByIdAndProblem(ideaId, problem)
            ?: throw CustomException(ProblemIdeaError.PROBLEM_IDEA_NOT_FOUND)

        return idea.rootComments.map { ProblemIdeaCommentResponse.of(it) }
    }

    @Transactional
    override fun createProblemIdeaComment(
        problemId: Long,
        ideaId: Long,
        request: ProblemIdeaCommentCreateRequest
    ): ProblemIdeaCommentResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val idea = problemIdeaRepository.findByIdAndProblem(ideaId, problem)
            ?: throw CustomException(ProblemIdeaError.PROBLEM_IDEA_NOT_FOUND)
        val author = securityHolder.user

        val parent = request.parentId?.let { parentId ->
            problemIdeaCommentRepository.findByIdOrNull(parentId)
                ?: throw CustomException(ProblemIdeaCommentError.PROBLEM_IDEA_COMMENT_NOT_FOUND)
        }

        var comment = ProblemIdeaComment(
            idea = idea,
            author = author,
            content = request.content,
            parent = parent
        )

        idea.addComment(comment)

        comment = problemIdeaCommentRepository.save(comment)
        return ProblemIdeaCommentResponse.of(comment)
    }

    @Transactional
    override fun updateProblemIdeaComment(
        problemId: Long,
        ideaId: Long,
        commentId: Long,
        request: ProblemIdeaCommentUpdateRequest
    ): ProblemIdeaCommentResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val idea = problemIdeaRepository.findByIdAndProblem(ideaId, problem)
            ?: throw CustomException(ProblemIdeaError.PROBLEM_IDEA_NOT_FOUND)
        val comment = problemIdeaCommentRepository.findByIdAndIdea(commentId, idea) ?: throw CustomException(
            ProblemIdeaCommentError.PROBLEM_IDEA_COMMENT_NOT_FOUND
        )

        if (comment.author != securityHolder.user) {
            throw CustomException(ProblemIdeaCommentError.PROBLEM_IDEA_COMMENT_NOT_AUTHORIZED)
        }

        request.content?.let { comment.content = it }

        return ProblemIdeaCommentResponse.of(comment)
    }

    @Transactional
    override fun deleteProblemIdeaComment(problemId: Long, ideaId: Long, commentId: Long) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val idea = problemIdeaRepository.findByIdAndProblem(ideaId, problem)
            ?: throw CustomException(ProblemIdeaError.PROBLEM_IDEA_NOT_FOUND)
        val comment = problemIdeaCommentRepository.findByIdAndIdea(commentId, idea) ?: throw CustomException(
            ProblemIdeaCommentError.PROBLEM_IDEA_COMMENT_NOT_FOUND
        )

        if (comment.author != securityHolder.user) {
            throw CustomException(ProblemIdeaCommentError.PROBLEM_IDEA_COMMENT_NOT_AUTHORIZED)
        }

        problemIdeaCommentRepository.delete(comment)
    }
}