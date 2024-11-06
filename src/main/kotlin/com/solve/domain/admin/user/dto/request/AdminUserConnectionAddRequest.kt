package com.solve.domain.admin.user.dto.request

import com.solve.domain.user.domain.enums.UserConnectionType

data class AdminUserConnectionAddRequest(
    val type: UserConnectionType,
    val value: String
)