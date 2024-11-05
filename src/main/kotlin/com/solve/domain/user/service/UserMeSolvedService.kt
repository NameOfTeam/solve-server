package com.solve.domain.user.service

import com.solve.domain.user.dto.response.UserMeSolvedResponse

interface UserMeSolvedService {
    fun getSolvedProblems(): List<UserMeSolvedResponse>
}