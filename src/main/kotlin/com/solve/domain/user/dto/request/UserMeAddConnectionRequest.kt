package com.solve.domain.user.dto.request

import com.solve.domain.user.domain.enums.UserConnectionType

data class UserMeAddConnectionRequest(
    val type: UserConnectionType,
    val value: String
)
