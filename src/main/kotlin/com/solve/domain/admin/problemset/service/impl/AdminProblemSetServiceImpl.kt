package com.solve.domain.admin.problemset.service.impl

import com.solve.domain.admin.problemset.dto.response.AdminProblemSetResponse
import com.solve.domain.admin.problemset.service.AdminProblemSetService
import com.solve.domain.problemset.repository.ProblemSetRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminProblemSetServiceImpl(
    private val problemSetRepository: ProblemSetRepository
) : AdminProblemSetService {
    @Transactional(readOnly = true)
    override fun getProblemSets(pageable: Pageable): Page<AdminProblemSetResponse> {
        return problemSetRepository.findAll(pageable).map { AdminProblemSetResponse.of(it) }
    }

    @Transactional(readOnly = true)
    override fun getProblemSet(problemSetId: Long): AdminProblemSetResponse {
        return AdminProblemSetResponse.of(problemSetRepository.findById(problemSetId).get())
    }
}