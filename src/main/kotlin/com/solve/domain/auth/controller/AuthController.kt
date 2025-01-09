package com.solve.domain.auth.controller

import com.solve.domain.auth.dto.request.LoginRequest
import com.solve.domain.auth.dto.request.ReissueRequest
import com.solve.domain.auth.dto.request.SignUpRequest
import com.solve.domain.auth.service.AuthService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
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
    fun login(@Valid @RequestBody request: LoginRequest) = BaseResponse.of(authService.login(request))

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignUpRequest) =
        BaseResponse.of(authService.signup(request), status = 201)

    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    fun reissue(@Valid @RequestBody request: ReissueRequest) =
        BaseResponse.of(authService.reissue(request), status = 201)

    @Operation(summary = "이메일 인증")
    @PostMapping("/verify")
    fun verify(@RequestParam token: String) = BaseResponse.of(authService.verify(token))
}