package com.solve.domain.auth.repository

interface RefreshTokenRepository {
    fun setRefreshToken(email: String, refreshToken: String)
    fun getRefreshToken(email: String): String?
    fun existsRefreshToken(email: String): Boolean
}