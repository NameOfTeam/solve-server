package com.devox.domain.user.domain.entity

import com.devox.domain.user.domain.enums.UserRole
import com.devox.domain.user.domain.generator.GeneratedUserId
import com.devox.global.common.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedUserId
    val id: Long? = null,

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole = UserRole.USER
) : BaseTimeEntity()