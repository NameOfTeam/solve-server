package com.devox.domain.auth.repository

import com.devox.domain.auth.domain.entity.RefreshToken
import org.springframework.data.repository.CrudRepository

interface RefreshTokenRepository : CrudRepository<RefreshToken, String>