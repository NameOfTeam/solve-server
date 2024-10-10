package com.solve.domain.user.controller

import com.solve.domain.user.service.UserService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "사용자", description = "User")
@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @Operation(summary = "나 조회", description = "내 정보를 조회합니다.")
    @GetMapping("/me")
    fun getMe() = BaseResponse.of(userService.getMe())
}