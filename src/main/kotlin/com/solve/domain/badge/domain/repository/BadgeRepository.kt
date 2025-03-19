package com.solve.domain.badge.domain.repository

import com.solve.domain.badge.domain.entity.Badge
import org.springframework.data.jpa.repository.JpaRepository

interface BadgeRepository: JpaRepository<Badge, Long> {
}