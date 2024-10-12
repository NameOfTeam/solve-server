package com.solve.domain.user.service

import com.solve.domain.user.dto.request.UserMeUpdateRequest
import com.solve.domain.user.dto.response.UserResponse

interface UserService {
    fun getMe(): UserResponse
    fun updateMe(request: UserMeUpdateRequest): UserResponse
}