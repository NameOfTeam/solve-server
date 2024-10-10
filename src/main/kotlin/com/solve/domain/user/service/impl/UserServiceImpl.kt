package com.solve.domain.user.service.impl

import com.solve.domain.user.dto.response.UserResponse
import com.solve.domain.user.service.UserService
import com.solve.global.security.holder.SecurityHolder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val securityHolder: SecurityHolder
) : UserService {
    override fun getMe(): UserResponse {
        val user = securityHolder.user

        return UserResponse(
            username = user.username,
            email = user.email,
            role = user.role
        )
    }
}