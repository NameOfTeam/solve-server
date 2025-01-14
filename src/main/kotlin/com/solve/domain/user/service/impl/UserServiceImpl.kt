package com.solve.domain.user.service.impl

import com.solve.domain.user.dto.response.UserResponse
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.domain.user.service.UserService
import com.solve.global.error.CustomException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
): UserService {
    @Transactional(readOnly = true)
    override fun getUser(username: String): UserResponse {
        val user = userRepository.findByUsername(username) ?: throw CustomException(
            UserError.USER_NOT_FOUND_BY_USERNAME,
            username
        )

        return UserResponse.of(user)
    }
}