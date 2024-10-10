package com.devox.domain.auth.controller

import com.devox.domain.auth.dto.request.LoginRequest
import com.devox.domain.auth.dto.request.ReissueRequest
import com.devox.domain.auth.dto.request.SignUpRequest
import com.devox.domain.auth.service.AuthService
import com.devox.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "인증", description = "Auth")
@CrossOrigin(origins = ["*"])
@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {
    @Operation(summary = "로그인")
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest) = BaseResponse.of(data = authService.login(request))

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    fun signup(@RequestBody request: SignUpRequest) = BaseResponse.of(data = authService.signup(request), status = 201)

    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    fun reissue(@RequestBody request: ReissueRequest) =
        BaseResponse.of(data = authService.reissue(request), status = 201)
}