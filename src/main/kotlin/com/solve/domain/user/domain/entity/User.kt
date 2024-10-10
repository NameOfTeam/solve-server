package com.solve.domain.user.domain.entity

import com.solve.domain.user.domain.enums.UserRole
import com.solve.global.common.BaseTimeEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "username", nullable = false, unique = true)
    val username: String,

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole = UserRole.USER
) : BaseTimeEntity()