package com.solve.domain.user.controller

import com.solve.domain.user.dto.request.UserMePasswordUpdateRequest
import com.solve.domain.user.dto.request.UserMeUpdateRequest
import com.solve.domain.user.service.UserMeService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Tag(name = "사용자 나", description = "User Me")
@RestController
@RequestMapping("/users/me")
class UserMeController(
    private val userMeService: UserMeService
) {
    @Operation(summary = "나 조회", description = "내 정보를 조회합니다.")
    @GetMapping
    fun getMe() = BaseResponse.of(userMeService.getMe())

    @Operation(summary = "나 수정", description = "내 정보를 수정합니다.")
    @PatchMapping
    fun updateMe(@RequestBody request: UserMeUpdateRequest) = BaseResponse.of(userMeService.updateMe(request))

    @Operation(summary = "비밀번호 수정", description = "내 비밀번호를 수정합니다.")
    @PatchMapping("/password")
    fun updatePassword(@RequestBody request: UserMePasswordUpdateRequest) =
        BaseResponse.of(userMeService.updatePassword(request))

    @Operation(summary = "아바타 수정", description = "내 아바타를 수정합니다.")
    @PatchMapping("/avatar", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateAvatar(@RequestPart("file") file: MultipartFile) = BaseResponse.of(userMeService.updateAvatar(file))
}