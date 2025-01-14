package com.solve.domain.theme.service

import com.solve.domain.theme.dto.response.ThemeResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ThemeService {
    fun getThemes(pageable: Pageable): Page<ThemeResponse>
}