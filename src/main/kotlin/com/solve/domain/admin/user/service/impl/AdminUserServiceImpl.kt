package com.solve.domain.admin.user.service.impl

import com.solve.domain.admin.user.dto.response.AdminUserResponse
import com.solve.domain.admin.user.service.AdminUserService
import com.solve.domain.user.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminUserServiceImpl(
    private val userRepository: UserRepository
) : AdminUserService {
    @Transactional(readOnly = true)
    override fun getUsers(pageable: Pageable): Slice<AdminUserResponse> {
        return userRepository.findAll(pageable).map { AdminUserResponse.of(it) }
    }
}