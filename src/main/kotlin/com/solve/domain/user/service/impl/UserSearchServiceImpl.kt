package com.solve.domain.user.service.impl

import com.solve.domain.user.dto.response.UserResponse
import com.solve.domain.user.repository.UserQueryRepository
import com.solve.domain.user.service.UserSearchService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserSearchServiceImpl(
    private val userQueryRepository: UserQueryRepository
): UserSearchService {
    @Transactional(readOnly = true)
    override fun searchUser(query: String, pageable: Pageable): Page<UserResponse> {
        val users = userQueryRepository.searchUser(query, pageable)

        return users.map { UserResponse.of(it) }
    }
}