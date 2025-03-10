package com.solve.domain.user.repository

import com.solve.domain.user.domain.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface UserQueryRepository {
    fun searchUser(query: String, pageable: Pageable): Page<User>
}