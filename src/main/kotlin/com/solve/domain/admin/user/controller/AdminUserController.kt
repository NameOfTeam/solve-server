package com.solve.domain.admin.user.controller

import com.solve.domain.admin.user.service.AdminUserService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "관리자: 사용자", description = "Admin: User")
@RestController
@RequestMapping("/admin/users")
class AdminUserController(
    private val adminUserService: AdminUserService
) {
    @Operation(summary = "사용자 목록 조회", description = "사용자 목록을 조회합니다.")
    @GetMapping
    fun getUsers(@PageableDefault pageable: Pageable) = BaseResponse.of(adminUserService.getUsers(pageable))

    @Operation(summary = "사용자 삭제")
    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: UUID) = BaseResponse.of(adminUserService.deleteUser(userId))
}