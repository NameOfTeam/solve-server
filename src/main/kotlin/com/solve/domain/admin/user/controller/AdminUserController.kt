package com.solve.domain.admin.user.controller

import com.solve.domain.admin.user.dto.request.AdminUserCreateRequest
import com.solve.domain.admin.user.dto.request.AdminUserUpdateRequest
import com.solve.domain.admin.user.service.AdminUserService
import com.solve.domain.user.domain.enums.UserRole
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Tag(name = "관리자: 사용자", description = "Admin: User")
@RestController
@RequestMapping("/admin/users")
class AdminUserController(
    private val adminUserService: AdminUserService
) {
    @Operation(summary = "사용자 목록 조회", description = "사용자 목록을 조회합니다.")
    @GetMapping
    fun getUsers(
        @PageableDefault pageable: Pageable,
        @RequestParam("search", required = false, defaultValue = "") search: String,
        @RequestParam("role", required = false, defaultValue = "USER") role: UserRole
    ) = BaseResponse.of(adminUserService.getUsers(pageable, search, role))

    @Operation(summary = "사용자 등록", description = "사용자를 등록합니다.")
    @PostMapping
    fun createUser(@RequestBody request: AdminUserCreateRequest) = BaseResponse.of(adminUserService.createUser(request))

    @Operation(summary = "사용자 상세 조회")
    @GetMapping("/{userId}")
    fun getUser(@PathVariable userId: UUID) = BaseResponse.of(adminUserService.getUser(userId))

    @Operation(summary = "사용자 수정")
    @PatchMapping("/{userId}")
    fun updateUser(@PathVariable userId: UUID, @RequestBody request: AdminUserUpdateRequest) =
        BaseResponse.of(adminUserService.updateUser(userId, request))

    @Operation(summary = "사용자 아바타 수정")
    @PatchMapping("/{userId}/avatar")
    fun updateUserAvatar(@PathVariable userId: UUID, @RequestPart("file") file: MultipartFile) =
        BaseResponse.of(adminUserService.updateUserAvatar(userId, file))

    @Operation(summary = "사용자 삭제")
    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: UUID) = BaseResponse.of(adminUserService.deleteUser(userId))
}