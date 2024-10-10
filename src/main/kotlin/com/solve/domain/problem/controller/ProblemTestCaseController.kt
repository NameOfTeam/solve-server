package com.solve.domain.problem.controller

import com.solve.domain.problem.dto.request.ProblemTestCaseCreateRequest
import com.solve.domain.problem.dto.request.ProblemTestCaseUpdateRequest
import com.solve.domain.problem.service.ProblemTestCaseService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "문제 테스트 케이스", description = "Problem Test Case")
@RestController
@RequestMapping("/problems/{problemId}/test-cases")
class ProblemTestCaseController(
    private val problemTestCaseService: ProblemTestCaseService
) {
    @Operation(summary = "문제 테스트 케이스 목록 조회", description = "문제의 테스��� 케이스 목록을 조회합니다.")
    @GetMapping
    fun getProblemTestCases(@PathVariable problemId: Long) =
        BaseResponse.of(problemTestCaseService.getProblemTestCases(problemId))

    @Operation(summary = "문제 테스트 케이스 추가", description = "문제에 테스트 케이스를 추가합니다.")
    @PostMapping
    fun addProblemTestCase(@PathVariable problemId: Long, @Valid @RequestBody request: ProblemTestCaseCreateRequest) =
        BaseResponse.of(problemTestCaseService.addProblemTestCase(problemId, request))

    @Operation(summary = "문제 테스트 케이스 수정", description = "문제의 테스트 케이스를 수정합니다.")
    @PatchMapping("/{testCaseId}")
    fun updateProblemTestCase(
        @PathVariable problemId: Long,
        @PathVariable testCaseId: Long,
        @Valid @RequestBody request: ProblemTestCaseUpdateRequest
    ) = BaseResponse.of(problemTestCaseService.updateProblemTestCase(problemId, testCaseId, request))

    @Operation(summary = "문제 테스트 케이스 삭제", description = "문제의 테스트 케이스를 삭제합니다.")
    @DeleteMapping("/{testCaseId}")
    fun removeProblemTestCase(@PathVariable problemId: Long, @PathVariable testCaseId: Long) =
        BaseResponse.of(problemTestCaseService.removeProblemTestCase(problemId, testCaseId))
}