package com.solve.domain.admin.contest.controller

import com.solve.domain.admin.contest.dto.request.AdminContestCreateRequest
import com.solve.domain.admin.contest.dto.request.AdminContestUpdateRequest
import com.solve.domain.admin.contest.service.AdminContestService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@Tag(name = "관리자: 대회", description = "Admin: Contest")
@RestController
@RequestMapping("/admin/contests")
class AdminContestController(
    private val adminContestService: AdminContestService
) {
    @Operation(summary = "대회 목록 조회")
    @GetMapping
    fun getContests(@PageableDefault pageable: Pageable) = BaseResponse.of(adminContestService.getContests(pageable))

    @Operation(summary = "대회 상세 조회")
    @GetMapping("/{contestId}")
    fun getContest(@PathVariable contestId: Long) = BaseResponse.of(adminContestService.getContest(contestId))

    @Operation(summary = "대회 생성")
    @PostMapping
    fun createContest(@RequestBody request: AdminContestCreateRequest) =
        BaseResponse.of(adminContestService.createContest(request), 201)

    @Operation(summary = "대회 수정")
    @PatchMapping("/{contestId}")
    fun updateContest(@PathVariable contestId: Long, @RequestBody request: AdminContestUpdateRequest) =
        BaseResponse.of(adminContestService.updateContest(contestId, request))

    @Operation(summary = "대회 삭제")
    @DeleteMapping("/{contestId}")
    fun deleteContest(@PathVariable contestId: Long) = BaseResponse.of(adminContestService.deleteContest(contestId))
}