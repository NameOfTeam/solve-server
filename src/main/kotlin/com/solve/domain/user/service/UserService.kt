package com.solve.domain.user.service

import com.solve.domain.user.dto.response.UserResponse

interface UserService {
    fun getUser(username: String): UserResponse
}