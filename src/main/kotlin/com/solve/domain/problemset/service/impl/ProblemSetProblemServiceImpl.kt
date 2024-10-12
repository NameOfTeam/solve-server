package com.solve.domain.problemset.service.impl

import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problemset.domain.entity.ProblemSetProblem
import com.solve.domain.problemset.dto.request.ProblemSetProblemAddRequest
import com.solve.domain.problemset.error.ProblemSetError
import com.solve.domain.problemset.repository.ProblemSetRepository
import com.solve.domain.problemset.service.ProblemSetProblemService
import com.solve.global.error.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemSetProblemServiceImpl(
    private val problemSetRepository: ProblemSetRepository,
    private val problemRepository: ProblemRepository
) : ProblemSetProblemService {
    @Transactional
    override fun addProblemSetProblem(problemSetId: Long, request: ProblemSetProblemAddRequest) {
        val problemSet = problemSetRepository.findByIdOrNull(problemSetId)
            ?: throw CustomException(ProblemSetError.PROBLEM_SET_NOT_FOUND)
        val problem =
            problemRepository.findByIdOrNull(request.problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        problemSet.problems.add(
            ProblemSetProblem(
                problemSet = problemSet,
                problem = problem
            )
        )
    }

    @Transactional
    override fun removeProblemSetProblem(problemSetId: Long, problemId: Long) {
        val problemSet = problemSetRepository.findByIdOrNull(problemSetId)
            ?: throw CustomException(ProblemSetError.PROBLEM_SET_NOT_FOUND)
        val problem = problemSet.problems.find { it.problem.id == problemId }
            ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        problemSet.problems.remove(problem)
    }
}