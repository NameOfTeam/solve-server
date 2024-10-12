package com.solve.domain.user.service.impl

import com.solve.domain.user.dto.request.UserMeUpdateRequest
import com.solve.domain.user.dto.response.UserResponse
import com.solve.domain.user.repository.UserRepository
import com.solve.domain.user.service.UserService
import com.solve.global.security.holder.SecurityHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val securityHolder: SecurityHolder,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val userRepository: UserRepository
) : UserService {
    @Transactional(readOnly = true)
    override fun getMe(): UserResponse {
        val user = securityHolder.user

        return UserResponse(
            username = user.username,
            email = user.email,
            role = user.role
        )
    }

    @Transactional
    override fun updateMe(request: UserMeUpdateRequest): UserResponse {
        var user = securityHolder.user

        if (request.username != null) user.username = request.username
        if (request.password != null && passwordEncoder.matches(request.currentPassword, user.password)) user.password =
            passwordEncoder.encode(request.password)

        user = userRepository.save(user)

        return UserResponse.of(user)
    }
}