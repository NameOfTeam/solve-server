package com.solve.domain.contest.controller

import com.solve.domain.contest.service.ContestParticipantService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "대회 참가자", description = "Contest Participant")
@RestController
@RequestMapping("/contests/{contestId}/participants")
class ContestParticipantController(
    private val contestParticipantService: ContestParticipantService
) {
    @Operation(summary = "대회 참가")
    @PostMapping
    fun joinContest(@PathVariable contestId: Long) =
        BaseResponse.of(contestParticipantService.joinContest(contestId), 201)

    @Operation(summary = "대회 참가 취소")
    @DeleteMapping
    fun leaveContest(@PathVariable contestId: Long) =
        BaseResponse.of(contestParticipantService.leaveContest(contestId), 204)
}