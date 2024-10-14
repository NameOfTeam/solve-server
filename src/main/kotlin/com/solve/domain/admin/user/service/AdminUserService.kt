package com.solve.domain.admin.user.service

import com.solve.domain.admin.user.dto.response.AdminUserResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface AdminUserService {
    fun getUsers(pageable: Pageable): Slice<AdminUserResponse>
}