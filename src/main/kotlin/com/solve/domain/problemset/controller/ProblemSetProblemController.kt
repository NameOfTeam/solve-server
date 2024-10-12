package com.solve.domain.problemset.controller

import com.solve.domain.problemset.dto.request.ProblemSetProblemAddRequest
import com.solve.domain.problemset.service.ProblemSetProblemService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "문제집 문제", description = "Problem Set Problem")
@RestController
@RequestMapping("/problem-sets/{problemSetId}/problems")
class ProblemSetProblemController(
    private val problemSetProblemService: ProblemSetProblemService
) {
    @Operation(summary = "문제집 문제 추가")
    @PostMapping
    fun addProblemSetProblem(@PathVariable problemSetId: Long, @RequestBody request: ProblemSetProblemAddRequest) =
        BaseResponse.of(problemSetProblemService.addProblemSetProblem(problemSetId, request))

    @Operation(summary = "문제집 문제 제거")
    @DeleteMapping("/{problemId}")
    fun removeProblemSetProblem(@PathVariable problemSetId: Long, @PathVariable problemId: Long) =
        BaseResponse.of(problemSetProblemService.removeProblemSetProblem(problemSetId, problemId))
}