package com.solve.domain.problemset.service.impl

import com.solve.domain.problemset.domain.entity.ProblemSet
import com.solve.domain.problemset.dto.request.ProblemSetCreateRequest
import com.solve.domain.problemset.dto.request.ProblemSetUpdateRequest
import com.solve.domain.problemset.dto.response.ProblemSetResponse
import com.solve.domain.problemset.error.ProblemSetError
import com.solve.domain.problemset.repository.ProblemSetRepository
import com.solve.domain.problemset.service.ProblemSetService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemSetServiceImpl(
    private val securityHolder: SecurityHolder,
    private val problemSetRepository: ProblemSetRepository
) : ProblemSetService {
    @Transactional(readOnly = true)
    override fun getProblemSets(pageable: Pageable): Page<ProblemSetResponse> {
        return problemSetRepository.findAll(pageable).map { ProblemSetResponse.of(it) }
    }

    @Transactional(readOnly = true)
    override fun getProblemSet(problemSetId: Long): ProblemSetResponse {
        val problemSet = problemSetRepository.findByIdOrNull(problemSetId)
            ?: throw CustomException(ProblemSetError.PROBLEM_SET_NOT_FOUND)

        return ProblemSetResponse.of(problemSet)
    }

    @Transactional
    override fun createProblemSet(request: ProblemSetCreateRequest): ProblemSetResponse {
        val author = securityHolder.user
        val problemSet = problemSetRepository.save(
            ProblemSet(
                title = request.title,
                author = author
            )
        )

        return ProblemSetResponse.of(problemSet)
    }

    @Transactional
    override fun updateProblemSet(problemSetId: Long, request: ProblemSetUpdateRequest): ProblemSetResponse {
        var problemSet = problemSetRepository.findByIdOrNull(problemSetId)
            ?: throw CustomException(ProblemSetError.PROBLEM_SET_NOT_FOUND)

        if (request.title != null) problemSet.title = request.title

        problemSet = problemSetRepository.save(problemSet)

        return ProblemSetResponse.of(problemSet)
    }

    @Transactional
    override fun deleteProblemSet(problemSetId: Long) {
        val problemSet = problemSetRepository.findByIdOrNull(problemSetId)
            ?: throw CustomException(ProblemSetError.PROBLEM_SET_NOT_FOUND)

        problemSetRepository.delete(problemSet)
    }
}