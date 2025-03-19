package com.solve.domain.user.repository

import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.domain.entity.UserBadge
import org.springframework.data.jpa.repository.JpaRepository

interface UserBadgeRepository: JpaRepository<UserBadge, Long> {
    fun findAllByUser(user: User): List<UserBadge>
}