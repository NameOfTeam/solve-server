package com.solve.domain.admin.user.dto.request

import com.solve.domain.user.domain.enums.UserRole

data class AdminUserUpdateRequest(
    val password: String?,
    val introduction: String?,
    val role: UserRole?
)