package com.solve.domain.theme.service.impl

import com.solve.domain.theme.domain.entity.Theme
import com.solve.domain.theme.dto.response.ThemeResponse
import com.solve.domain.theme.repository.ThemeRepository
import com.solve.domain.theme.service.ThemeService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ThemeServiceImpl(
    private val themeRepository: ThemeRepository
): ThemeService {
    @Transactional(readOnly = true)
    override fun getThemes(pageable: Pageable): Page<ThemeResponse> {
        val themes = themeRepository.findAll(pageable)

        return themes.map { it.toResponse() }
    }

    private fun Theme.toResponse() = ThemeResponse(
        id = id!!,
        name = name,
        description = description,
        thumbnail = thumbnail,
        background = background,
        backgroundBorder = backgroundBorder,
        container = container,
        containerBorder = containerBorder,
        main = main,
        price = price,
        isPurchasable = true,
        has = false,
    )
}