package com.solve.domain.user.dto.request

data class UserMeUpdateRequest(
    val username: String?,
    val introduction: String?,
    val gender: String?
)