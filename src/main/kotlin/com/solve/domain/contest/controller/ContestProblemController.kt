package com.solve.domain.contest.controller

import com.solve.domain.contest.dto.request.ContestProblemAddRequest
import com.solve.domain.contest.service.ContestProblemService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "대회 문제", description = "Contest Problem")
@RestController
@RequestMapping("/contests/{contestId}/problems")
class ContestProblemController(
    private val contestProblemService: ContestProblemService
) {
    @Operation(summary = "대회 문제 추가")
    @PostMapping
    fun addContestProblem(@PathVariable contestId: Long, @RequestBody request: ContestProblemAddRequest) =
        BaseResponse.of(contestProblemService.addContestProblem(contestId, request))

    @Operation(summary = "대회 문제 삭제")
    @DeleteMapping("/{problemId}")
    fun removeContestProblem(@PathVariable contestId: Long, @PathVariable problemId: Long) =
        BaseResponse.of(contestProblemService.removeContestProblem(contestId, problemId))
}