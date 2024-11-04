package com.solve.domain.problem.controller

import com.solve.domain.problem.service.ProblemService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}