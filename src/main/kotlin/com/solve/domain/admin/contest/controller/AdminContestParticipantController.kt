package com.solve.domain.admin.contest.controller

import com.solve.domain.admin.contest.dto.request.AdminContestParticipantAddRequest
import com.solve.domain.admin.contest.service.AdminContestParticipantService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "관리자: 대회 참가자", description = "Contest Participant")
@RestController
@RequestMapping("/admin/contests/{contestId}/participants")
class AdminContestParticipantController(
    private val adminContestParticipantService: AdminContestParticipantService
) {
    @Operation(summary = "대회 참가자 추가")
    @PostMapping
    fun addContestParticipant(@PathVariable contestId: Long, @RequestBody request: AdminContestParticipantAddRequest) =
        BaseResponse.of(adminContestParticipantService.addContestParticipant(contestId, request))

    @Operation(summary = "대회 참가자 삭제")
    @DeleteMapping("/{userId}")
    fun removeContestParticipant(@PathVariable contestId: Long, @PathVariable userId: UUID) =
        BaseResponse.of(adminContestParticipantService.removeContestParticipant(contestId, userId))
}