package com.solve.domain.user.domain.entity

import com.solve.domain.user.domain.enums.UserRole
import com.solve.global.common.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "username", nullable = false, unique = true)
    var username: String,

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    val email: String,

    @Column(name = "password", nullable = false)
    var password: String,

//    @Column(name = "rating", nullable = false)
//    var rating: Int = 0,
//
//    @Column(name = "rank", nullable = false)
//    var rank: UserRank = UserRank.ROOKIE,

    @Column(name = "streak", nullable = false)
    var streak: Int = 0,

    @Column(name = "last_accepted_at")
    var lastAcceptedAt: LocalDate? = null,

    @Column(name = "verified", nullable = false)
    var verified: Boolean = false,

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    val role: UserRole = UserRole.USER
) : BaseTimeEntity()