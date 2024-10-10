package com.devox.domain.user.service.impl

import com.devox.domain.user.dto.response.GetUserResponse
import com.devox.domain.user.service.UserService
import com.devox.global.security.holder.SecurityHolder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(
    private val securityHolder: SecurityHolder
) : UserService {
    override fun getMe(): GetUserResponse {
        val user = securityHolder.user

        return GetUserResponse(
            email = user.email,
            role = user.role
        )
    }
}