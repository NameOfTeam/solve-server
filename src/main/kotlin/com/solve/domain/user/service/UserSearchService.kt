package com.solve.domain.user.service

import com.solve.domain.user.dto.response.UserResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserSearchService {
    fun searchUser(query: String, pageable: Pageable): Page<UserResponse>
}