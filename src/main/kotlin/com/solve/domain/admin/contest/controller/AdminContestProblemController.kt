package com.solve.domain.admin.contest.controller

import com.solve.domain.admin.contest.dto.request.AdminContestProblemAddRequest
import com.solve.domain.admin.contest.service.AdminContestProblemService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "관리자: 대회 문제", description = "Admin: Contest Problem")
@RestController
@RequestMapping("/admin/contests/{contestId}/problems")
class AdminContestProblemController(
    private val adminContestProblemService: AdminContestProblemService
) {
    @Operation(summary = "대회 문제 추가")
    @PostMapping
    fun addContestProblem(@PathVariable contestId: Long, @RequestBody request: AdminContestProblemAddRequest) =
        BaseResponse.of(adminContestProblemService.addContestProblem(contestId, request))

    @Operation(summary = "대회 문제 삭제")
    @DeleteMapping("/{problemId}")
    fun removeContestProblem(@PathVariable contestId: Long, @PathVariable problemId: Long) =
        BaseResponse.of(adminContestProblemService.removeContestProblem(contestId, problemId))
}