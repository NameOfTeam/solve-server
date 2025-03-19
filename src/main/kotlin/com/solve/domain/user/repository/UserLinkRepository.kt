package com.solve.domain.user.repository

import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.domain.entity.UserLink
import org.springframework.data.jpa.repository.JpaRepository

interface UserLinkRepository: JpaRepository<UserLink, Long> {
    fun findAllByUser(user: User): List<UserLink>
}