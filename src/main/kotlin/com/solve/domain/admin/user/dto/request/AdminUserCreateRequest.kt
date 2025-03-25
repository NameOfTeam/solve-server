package com.solve.domain.admin.user.dto.request

import com.solve.domain.user.domain.enums.UserRole
import java.time.LocalDate

data class AdminUserCreateRequest(
    val username: String,
    val email: String,
    val password: String,
    val gender: String?,
    val birth: LocalDate?,
    val role: UserRole?
)