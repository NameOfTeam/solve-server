package com.solve.domain.admin.auth.service

import com.solve.domain.admin.auth.dto.request.AdminSignUpRequest

interface AdminAuthService {
    fun signup(request: AdminSignUpRequest)
}