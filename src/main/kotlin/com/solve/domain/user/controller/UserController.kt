package com.solve.domain.user.controller

import com.solve.domain.user.dto.request.UserMeUpdateRequest
import com.solve.domain.user.service.UserService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "사용자", description = "User")
@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {
    @Operation(summary = "나 조회", description = "내 정보를 조회합니다.")
    @GetMapping("/me")
    fun getMe() = BaseResponse.of(userService.getMe())

    @Operation(summary = "나 수정", description = "내 정보를 수정합니다.")
    @PatchMapping("/me")
    fun updateMe(@RequestBody request: UserMeUpdateRequest) = BaseResponse.of(userService.updateMe(request))
}