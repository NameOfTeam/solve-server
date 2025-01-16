package com.solve.domain.theme.repository

import com.solve.domain.theme.domain.entity.Theme
import org.springframework.data.jpa.repository.JpaRepository

interface ThemeRepository : JpaRepository<Theme, Long>