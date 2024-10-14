package com.solve.domain.auth.domain.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "email_verifications")
class EmailVerification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "verification_token", nullable = false, unique = true, updatable = false)
    var verificationToken: String,

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    var email: String,

    @Column(name = "expired_at", nullable = false, updatable = false)
    var expiredAt: LocalDateTime
)