package com.solve.domain.admin.problem.controller

import com.solve.domain.admin.problem.dto.request.AdminProblemExampleCreateRequest
import com.solve.domain.admin.problem.service.AdminProblemExampleService
import com.solve.domain.admin.problem.dto.request.AdminProblemExampleUpdateRequest
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "관리자: 문제 예시 입출력", description = "Admin: Problem Example")
@RestController
@RequestMapping("/admin/problems/{problemId}/examples")
class AdminProblemExampleController(
    private val adminProblemExampleService: AdminProblemExampleService
) {
    @Operation(summary = "문제 예시 입출력 목록 조회", description = "문제의 예시 입출력 목록을 조회합니다.")
    @GetMapping
    fun getProblemExamples(@PathVariable problemId: Long) =
        BaseResponse.of(adminProblemExampleService.getProblemExamples(problemId))

    @Operation(summary = "문제 예시 입출력 추가", description = "문제에 예시 입출력를 추가합니다.")
    @PostMapping
    fun addProblemExample(@PathVariable problemId: Long, @Valid @RequestBody request: AdminProblemExampleCreateRequest) =
        BaseResponse.of(adminProblemExampleService.addProblemExample(problemId, request))

    @Operation(summary = "문제 예시 입출력 수정", description = "문제의 예시 입출력를 수정합니다.")
    @PatchMapping("/{exampleId}")
    fun updateProblemExample(
        @PathVariable problemId: Long,
        @PathVariable exampleId: Long,
        @Valid @RequestBody request: AdminProblemExampleUpdateRequest
    ) = BaseResponse.of(adminProblemExampleService.updateProblemExample(problemId, exampleId, request))

    @Operation(summary = "문제 예시 입출력 삭제", description = "문제의 예시 입출력를 삭제합니다.")
    @DeleteMapping("/{exampleId}")
    fun removeProblemExample(@PathVariable problemId: Long, @PathVariable exampleId: Long) =
        BaseResponse.of(adminProblemExampleService.removeProblemExample(problemId, exampleId))
}