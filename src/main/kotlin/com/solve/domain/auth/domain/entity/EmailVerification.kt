package com.solve.domain.auth.domain.entity

import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "email_verifications")
class EmailVerification(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "verification_token", nullable = false, unique = true, updatable = false)
    var verificationToken: String,

    @Column(name = "email", nullable = false, unique = true, updatable = false)
    var email: String,

    @Column(name = "expired_at", nullable = false, updatable = false)
    var expiredAt: LocalDateTime,

    @Column(name = "is_verified", nullable = false)
    var isVerified: Boolean = false
) : BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EmailVerification) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}