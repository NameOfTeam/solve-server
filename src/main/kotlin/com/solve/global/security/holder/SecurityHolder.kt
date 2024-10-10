package com.devox.global.security.holder

import com.devox.domain.user.domain.entity.User

interface SecurityHolder {
    val email: String
    val user: User
}