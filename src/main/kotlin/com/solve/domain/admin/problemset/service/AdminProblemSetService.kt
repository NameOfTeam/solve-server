package com.solve.domain.admin.problemset.service

import com.solve.domain.admin.problemset.dto.response.AdminProblemSetResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface AdminProblemSetService {
    fun getProblemSets(pageable: Pageable): Page<AdminProblemSetResponse>
    fun getProblemSet(problemSetId: Long): AdminProblemSetResponse
}