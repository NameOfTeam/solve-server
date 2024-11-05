package com.solve.domain.user.service.impl

import com.solve.domain.user.dto.response.UserMeSolvedResponse
import com.solve.domain.user.service.UserMeSolvedService
import com.solve.global.security.holder.SecurityHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserMeSolvedServiceImpl(
    private val securityHolder: SecurityHolder
) : UserMeSolvedService {
    @Transactional(readOnly = true)
    override fun getSolvedProblems(): List<UserMeSolvedResponse> {
        val user = securityHolder.user
        val solvedProblems = user.solved

        return solvedProblems.map { UserMeSolvedResponse.of(it) }
    }
}