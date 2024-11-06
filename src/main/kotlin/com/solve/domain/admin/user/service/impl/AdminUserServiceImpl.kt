package com.solve.domain.admin.user.service.impl

import com.solve.domain.admin.user.dto.request.AdminUserUpdateRequest
import com.solve.domain.admin.user.dto.response.AdminUserResponse
import com.solve.domain.admin.user.service.AdminUserService
import com.solve.domain.user.domain.enums.UserRole
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AdminUserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : AdminUserService {
    @Transactional(readOnly = true)
    override fun getUsers(pageable: Pageable, search: String, role: UserRole): Slice<AdminUserResponse> {
        val users = userRepository.findAllByRoleAndUsernameContainsIgnoreCaseOrEmailContainsIgnoreCase(
            pageable,
            role,
            search,
            search
        )

        return users.map { AdminUserResponse.of(it) }
    }

    @Transactional(readOnly = true)
    override fun getUser(userId: UUID): AdminUserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw CustomException(
            UserError.USER_NOT_FOUND_BY_ID,
            userId.toString()
        )

        return AdminUserResponse.of(user)
    }

    @Transactional
    override fun updateUser(userId: UUID, request: AdminUserUpdateRequest): AdminUserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw CustomException(
            UserError.USER_NOT_FOUND_BY_ID,
            userId.toString()
        )

        request.password?.let { user.password = passwordEncoder.encode(it) }
        request.role?.let { user.role = it }

        return AdminUserResponse.of(user)
    }

    @Transactional
    override fun deleteUser(userId: UUID): AdminUserResponse {
        val user = userRepository.findByIdOrNull(userId) ?: throw CustomException(
            UserError.USER_NOT_FOUND_BY_ID,
            userId.toString()
        )

        userRepository.delete(user)

        return AdminUserResponse.of(user)
    }
}