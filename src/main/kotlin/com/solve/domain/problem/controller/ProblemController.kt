package com.solve.domain.problem.controller

import com.solve.domain.problem.dto.request.ProblemUpdateRequest
import com.solve.domain.problem.service.ProblemService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@Tag(name = "문제", description = "Problem")
@RestController
@RequestMapping("/problems")
class ProblemController(
    private val problemService: ProblemService
) {
    @Operation(summary = "문제 목록 조회", description = "문제 목록을 조회합니다.")
    @GetMapping
    fun getProblems(@PageableDefault pageable: Pageable) = BaseResponse.of(problemService.getProblems(pageable))

    @Operation(summary = "문제 상세 조회", description = "문제 상세를 조회합니다.")
    @GetMapping("/{problemId}")
    fun getProblem(@PathVariable problemId: Long) = BaseResponse.of(problemService.getProblem(problemId))

    @Operation(summary = "문제 수정", description = "문제를 수정합니다.")
    @PatchMapping("/{problemId}")
    fun updateProblem(@PathVariable problemId: Long, @Valid @RequestBody request: ProblemUpdateRequest) =
        BaseResponse.of(problemService.updateProblem(problemId, request))

    @Operation(summary = "문제 삭제", description = "문제를 삭제합니다.")
    @DeleteMapping("/{problemId}")
    fun deleteProblem(@PathVariable problemId: Long) = BaseResponse.of(problemService.deleteProblem(problemId))
}