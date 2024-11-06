package com.solve.domain.admin.user.controller

import com.solve.domain.admin.user.dto.request.AdminUserConnectionAddRequest
import com.solve.domain.admin.user.service.AdminUserConnectionService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "관리자: 사용자 연결", description = "Admin: User Connection")
@RestController
@RequestMapping("/admin/users/{userId}/connections")
class AdminUserConnectionController(
    private val adminUserConnectionService: AdminUserConnectionService
) {
    @Operation(summary = "사용자 연결 추가")
    @PostMapping
    fun addUserConnection(@PathVariable userId: UUID, @RequestBody request: AdminUserConnectionAddRequest) = BaseResponse.of(adminUserConnectionService.addUserConnection(userId, request), 201)

    @Operation(summary = "사용자 연결 삭제")
    @DeleteMapping("/{connectionId}")
    fun removeUserConnection(@PathVariable userId: UUID, @PathVariable connectionId: UUID) = BaseResponse.of(adminUserConnectionService.removeUserConnection(userId, connectionId), 204)
}