package com.solve.domain.contest.controller

import com.solve.domain.contest.service.ContestService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@Tag(name = "대회", description = "Contest")
@RestController
@RequestMapping("/contests")
class ContestController(
    private val contestService: ContestService
) {
    @Operation(summary = "대회 목록 조회", description = "대회 목록을 조회합니다.")
    @GetMapping
    fun getContests(@PageableDefault pageable: Pageable) = BaseResponse.of(contestService.getContests(pageable))

    @Operation(summary = "대회 상세 조회", description = "대회 상세를 조회합니다.")
    @GetMapping("/{contestId}")
    fun getContest(@PathVariable contestId: Long) = BaseResponse.of(contestService.getContest(contestId))

    @Operation(summary = "대회 생성", description = "대회를 생성합니다.")
    @PostMapping
    fun createContest() {
        TODO()
    }

    @Operation(summary = "대회 수정", description = "대회를 수정합니다.")
    @PatchMapping("/{contestId}")
    fun updateContest(@PathVariable contestId: Long) {
        TODO()
    }

    @Operation(summary = "대회 삭제", description = "대회를 삭제합니다.")
    @DeleteMapping("/{contestId}")
    fun deleteContest(@PathVariable contestId: Long) {
        TODO()
    }
}