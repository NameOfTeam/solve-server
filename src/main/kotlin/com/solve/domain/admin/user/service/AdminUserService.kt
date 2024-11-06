package com.solve.domain.admin.user.service

import com.solve.domain.admin.user.dto.request.AdminUserUpdateRequest
import com.solve.domain.admin.user.dto.response.AdminUserResponse
import com.solve.domain.user.domain.enums.UserRole
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.web.multipart.MultipartFile
import java.util.*

interface AdminUserService {
    fun getUsers(pageable: Pageable, search: String, role: UserRole): Slice<AdminUserResponse>
    fun getUser(userId: UUID): AdminUserResponse
    fun updateUser(userId: UUID, request: AdminUserUpdateRequest): AdminUserResponse
    fun updateUserAvatar(userId: UUID, file: MultipartFile): AdminUserResponse
    fun deleteUser(userId: UUID): AdminUserResponse
}