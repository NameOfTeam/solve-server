package com.solve.domain.auth.repository

interface VerificationTokenRepository {
    fun setVerificationToken(verificationToken: String, email: String)
    fun getVerificationToken(verificationToken: String): String?
    fun existsVerificationToken(verificationToken: String): Boolean
    fun deleteVerificationToken(verificationToken: String)
}