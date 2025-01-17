package com.solve.domain.theme.mapper

import com.solve.domain.theme.domain.entity.Theme
import com.solve.domain.theme.dto.response.ThemeResponse
import org.springframework.stereotype.Component

@Component
class ThemeMapper {
    fun toResponse(theme: Theme) = ThemeResponse(
        id = theme.id!!,
        name = theme.name,
        description = theme.description,
        thumbnail = theme.thumbnail,
        background = theme.background,
        backgroundBorder = theme.backgroundBorder,
        container = theme.container,
        containerBorder = theme.containerBorder,
        main = theme.main,
        price = theme.price,
        isPurchasable = false,
        has = false
    )
}