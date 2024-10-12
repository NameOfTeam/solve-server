package com.solve.domain.problemset.controller

import com.solve.domain.problemset.dto.request.ProblemSetCreateRequest
import com.solve.domain.problemset.dto.request.ProblemSetUpdateRequest
import com.solve.domain.problemset.service.ProblemSetService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@Tag(name = "문제집", description = "Problem Set")
@RestController
@RequestMapping("/problem-sets")
class ProblemSetController(
    private val problemSetService: ProblemSetService
) {
    @Operation(summary = "문제집 목록 조회")
    @GetMapping
    fun getProblemSets(@PageableDefault pageable: Pageable) =
        BaseResponse.of(problemSetService.getProblemSets(pageable))

    @Operation(summary = "문제집 상세 조회")
    @GetMapping("/{problemSetId}")
    fun getProblem(@PathVariable problemSetId: Long) = BaseResponse.of(problemSetService.getProblemSet(problemSetId))

    @Operation(summary = "문제집 생성")
    @PostMapping
    fun createProblem(@RequestBody request: ProblemSetCreateRequest) =
        BaseResponse.of(problemSetService.createProblemSet(request))

    @Operation(summary = "문제집 수정")
    @PatchMapping("/{problemSetId}")
    fun updateProblem(@PathVariable problemSetId: Long, @RequestBody request: ProblemSetUpdateRequest) =
        BaseResponse.of(problemSetService.updateProblemSet(problemSetId, request))

    @Operation(summary = "문제집 삭제")
    @DeleteMapping("/{problemSetId}")
    fun deleteProblem(@PathVariable problemSetId: Long) =
        BaseResponse.of(problemSetService.deleteProblemSet(problemSetId))
}