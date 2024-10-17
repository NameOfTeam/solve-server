package com.solve.domain.contest.controller

import com.solve.domain.contest.dto.request.ContestParticipantAddRequest
import com.solve.domain.contest.service.ContestParticipantService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.util.*

@Tag(name = "대회 참가자", description = "Contest Participant")
@RestController
@RequestMapping("/contests/{contestId}/participants")
class ContestParticipantController(
    private val contestParticipantService: ContestParticipantService
) {
    @Operation(summary = "대회 참가자 추가")
    @PostMapping
    fun addContestParticipant(@PathVariable contestId: Long, @RequestBody request: ContestParticipantAddRequest) =
        BaseResponse.of(contestParticipantService.addContestParticipant(contestId, request))

    @Operation(summary = "대회 참가자 삭제")
    @DeleteMapping("/{userId}")
    fun removeContestParticipant(@PathVariable contestId: Long, @PathVariable userId: UUID) =
        BaseResponse.of(contestParticipantService.removeContestParticipant(contestId, userId))
}