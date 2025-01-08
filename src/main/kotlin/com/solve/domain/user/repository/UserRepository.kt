package com.solve.domain.user.repository

import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.domain.enums.UserRole
import com.solve.global.common.enums.Tier
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, UUID> {
    fun findByUsername(username: String): User?
    fun findByEmail(email: String): User?

    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean

    fun findAllByRoleAndUsernameContainsIgnoreCaseOrEmailContainsIgnoreCase(
        pageable: Pageable,
        role: UserRole,
        username: String,
        email: String
    ): Page<User>

    fun countByTierIsNot(tier: Tier): Long
}