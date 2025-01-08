package com.solve.domain.admin.auth.controller

import com.solve.domain.admin.auth.dto.request.AdminSignUpRequest
import com.solve.domain.admin.auth.service.AdminAuthService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "관리자: 인증", description = "Admin: Auth")
@RestController
@RequestMapping("/admin/auth")
class AdminAuthController(
    private val adminAuthService: AdminAuthService
) {
    @Operation(summary = "회원가입", description = "관리자 회원가입을 합니다.")
    @PostMapping("/signup")
    fun signup(@RequestBody request: AdminSignUpRequest) = BaseResponse.of(adminAuthService.signup(request))
}