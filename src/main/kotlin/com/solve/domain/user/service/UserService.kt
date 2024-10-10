package com.devox.domain.user.service

import com.devox.domain.user.dto.response.GetUserResponse

interface UserService {
    fun getMe(): GetUserResponse
}