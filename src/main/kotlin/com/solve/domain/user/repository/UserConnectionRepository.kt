package com.solve.domain.user.repository

import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.domain.entity.UserConnection
import com.solve.domain.user.domain.enums.UserConnectionType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserConnectionRepository : JpaRepository<UserConnection, Long> {
    fun findByUserAndId(user: User, id: UUID): UserConnection?
    fun findByUserAndType(user: User, type: UserConnectionType): UserConnection?
    fun findAllByUser(user: User): List<UserConnection>
}