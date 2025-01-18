package com.solve.domain.theme.service

import com.solve.domain.theme.dto.response.ThemeResponse
import com.solve.domain.theme.mapper.ThemeMapper
import com.solve.domain.theme.repository.ThemeQueryRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ThemeSearchService(
    private val themeMapper: ThemeMapper,
    private val themeQueryRepository: ThemeQueryRepository
) {
    @Transactional(readOnly = true)
    fun searchTheme(query: String, pageable: Pageable): Page<ThemeResponse> {
        val themes = themeQueryRepository.searchTheme(query, pageable)

        return themes.map { themeMapper.toResponse(it) }
    }
}