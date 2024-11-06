package com.solve.domain.admin.user.service

import com.solve.domain.admin.user.dto.request.AdminUserConnectionAddRequest
import java.util.*

interface AdminUserConnectionService {
    fun addUserConnection(userId: UUID, request: AdminUserConnectionAddRequest)
    fun removeUserConnection(userId: UUID, connectionId: UUID)
}