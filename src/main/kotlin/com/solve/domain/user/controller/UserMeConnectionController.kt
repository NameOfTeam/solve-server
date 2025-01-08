package com.solve.domain.user.controller

import com.solve.domain.user.dto.request.UserMeAddConnectionRequest
import com.solve.domain.user.service.UserMeConnectionService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "사용자 나 연결", description = "User Me Connection")
@RestController
@RequestMapping("/users/me/connections")
class UserMeConnectionController(
    private val userMeConnectionService: UserMeConnectionService
) {
    @Operation(summary = "연결 추가", description = "내 연결을 추가합니다.")
    @PostMapping
    fun addConnection(@RequestBody request: UserMeAddConnectionRequest) =
        BaseResponse.of(userMeConnectionService.addConnection(request))

    @Operation(summary = "연결 삭제", description = "내 연결을 삭제합니다.")
    @DeleteMapping("/{connectionId}")
    fun deleteConnection(@PathVariable connectionId: UUID) =
        BaseResponse.of(userMeConnectionService.removeConnection(connectionId))
}