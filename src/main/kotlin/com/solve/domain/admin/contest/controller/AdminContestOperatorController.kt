package com.solve.domain.admin.contest.controller

import com.solve.domain.admin.contest.dto.request.AdminContestOperatorAddRequest
import com.solve.domain.admin.contest.service.AdminContestOperatorService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "관리자: 대회 운영자", description = "Admin: Contest Operator")
@RestController
@RequestMapping("/admin/contests/{contestId}/operators")
class AdminContestOperatorController(
    private val adminContestOperatorService: AdminContestOperatorService
) {
    @Operation(summary = "대회 운영자 추가")
    @PostMapping
    fun addContestOperator(@PathVariable contestId: Long, @RequestBody request: AdminContestOperatorAddRequest) =
        BaseResponse.of(adminContestOperatorService.addContestOperator(contestId, request))

    @Operation(summary = "대회 운영자 삭제")
    @DeleteMapping("/{userId}")
    fun removeContestOperator(@PathVariable contestId: Long, @PathVariable userId: UUID) =
        BaseResponse.of(adminContestOperatorService.removeContestOperator(contestId, userId))
}