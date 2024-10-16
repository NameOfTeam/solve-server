package com.solve.domain.admin.user.service.impl

import com.solve.domain.admin.user.dto.response.AdminUserResponse
import com.solve.domain.admin.user.service.AdminUserService
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AdminUserServiceImpl(
    private val userRepository: UserRepository
) : AdminUserService {
    @Transactional(readOnly = true)
    override fun getUsers(pageable: Pageable): Slice<AdminUserResponse> {
        return userRepository.findAll(pageable).map { AdminUserResponse.of(it) }
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