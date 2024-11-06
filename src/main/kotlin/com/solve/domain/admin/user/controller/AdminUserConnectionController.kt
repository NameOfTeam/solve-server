package com.solve.domain.admin.user.controller

import com.solve.domain.admin.user.dto.request.AdminUserConnectionAddRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "관리자: 사용자 연결", description = "Admin: User Connection")
@RestController
@RequestMapping("/admin/users/{userId}/connections")
class AdminUserConnectionController {
    @Operation(summary = "사용자 연결 추가")
    @PostMapping
    fun addUserConnection(@PathVariable userId: UUID, @RequestBody request: AdminUserConnectionAddRequest) {
    }

    @Operation(summary = "사용자 연결 삭제")
    @DeleteMapping("/{connectionId}")
    fun deleteUserConnection(@PathVariable userId: UUID, @PathVariable connectionId: UUID) {
    }
}