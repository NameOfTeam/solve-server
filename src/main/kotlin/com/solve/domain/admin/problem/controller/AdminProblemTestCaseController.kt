package com.solve.domain.admin.problem.controller

import com.solve.domain.admin.problem.dto.request.AdminProblemTestCaseCreateRequest
import com.solve.domain.admin.problem.dto.request.AdminProblemTestCaseUpdateRequest
import com.solve.domain.admin.problem.service.AdminProblemTestCaseService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "관리자: 문제 테스트 케이스", description = "Admin: Problem Test Case")
@RestController
@RequestMapping("/admin/problems/{problemId}/test-cases")
class AdminProblemTestCaseController(
    private val adminProblemTestCaseService: AdminProblemTestCaseService
) {
    @Operation(summary = "문제 테스트 케이스 목록 조회", description = "문제의 테스트 케이스 목록을 조회합니다.")
    @GetMapping
    fun getProblemTestCases(@PathVariable problemId: Long) =
        BaseResponse.of(adminProblemTestCaseService.getProblemTestCases(problemId))

    @Operation(summary = "문제 테스트 케이스 추가", description = "문제에 테스트 케이스를 추가합니다.")
    @PostMapping
    fun addProblemTestCase(
        @PathVariable problemId: Long,
        @Valid @RequestBody request: AdminProblemTestCaseCreateRequest
    ) =
        BaseResponse.of(adminProblemTestCaseService.addProblemTestCase(problemId, request))

    @Operation(summary = "문제 테스트 케이스 수정", description = "문제의 테스트 케이스를 수정합니다.")
    @PatchMapping("/{testCaseId}")
    fun updateProblemTestCase(
        @PathVariable problemId: Long,
        @PathVariable testCaseId: Long,
        @Valid @RequestBody request: AdminProblemTestCaseUpdateRequest
    ) = BaseResponse.of(adminProblemTestCaseService.updateProblemTestCase(problemId, testCaseId, request))

    @Operation(summary = "문제 테스트 케이스 삭제", description = "문제의 테스트 케이스를 삭제합니다.")
    @DeleteMapping("/{testCaseId}")
    fun removeProblemTestCase(@PathVariable problemId: Long, @PathVariable testCaseId: Long) =
        BaseResponse.of(adminProblemTestCaseService.removeProblemTestCase(problemId, testCaseId))
}