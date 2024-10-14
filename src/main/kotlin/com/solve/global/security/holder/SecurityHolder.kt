package com.solve.global.security.holder

import com.solve.domain.user.domain.entity.User

interface SecurityHolder {
    val email: String
    val user: User
    val isAuthenticated: Boolean
}