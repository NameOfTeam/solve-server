package com.solve.domain.badge.presentation.dto.response

data class BadgeResponse(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val condition: String
)