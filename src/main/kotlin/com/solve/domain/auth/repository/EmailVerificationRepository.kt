package com.solve.domain.auth.repository

import com.solve.domain.auth.domain.entity.EmailVerification
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface EmailVerificationRepository : JpaRepository<EmailVerification, String> {
    fun findAllByExpiredAtBeforeAndVerifiedFalse(now: LocalDateTime): List<EmailVerification>

    fun findByVerificationToken(verificationToken: String): EmailVerification?
    fun existsByVerificationToken(verificationToken: String): Boolean
}