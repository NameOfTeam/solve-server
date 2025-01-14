package com.solve.domain.theme.dto.response

data class ThemeResponse(
    val id: Long,
    val name: String,
    val description: String,
    val thumbnail: String,
    val background: String,
    val backgroundBorder: String,
    val container: String,
    val containerBorder: String,
    val main: String,
    val price: Int,
    val isPurchasable: Boolean,
    val has: Boolean,
)