package com.solve.domain.admin.problem.controller

import com.solve.domain.admin.problem.dto.request.AdminProblemCreateRequest
import com.solve.domain.admin.problem.dto.request.AdminProblemUpdateRequest
import com.solve.domain.admin.problem.service.AdminProblemService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.*

@Tag(name = "관리자: 문제", description = "Admin: Problem")
@RestController
@RequestMapping("/admin/problems")
class AdminProblemController(
    private val adminProblemService: AdminProblemService
) {
    @Operation(summary = "문제 목록 조회", description = "문제 목록을 조회합니다.")
    @GetMapping
    fun getProblems(@PageableDefault pageable: Pageable) = BaseResponse.of(adminProblemService.getProblems(pageable))

    @Operation(summary = "문제 조회")
    @GetMapping("/{problemId}")
    fun getProblem(@PathVariable problemId: Long) = BaseResponse.of(adminProblemService.getProblem(problemId))

    @Operation(summary = "문제 생성", description = "문제를 생성합니다.")
    @PostMapping
    fun createProblem(@Valid @RequestBody request: AdminProblemCreateRequest) =
        BaseResponse.of(adminProblemService.createProblem(request))

    @Operation(summary = "문제 수정", description = "문제를 수정합니다.")
    @PatchMapping("/{problemId}")
    fun updateProblem(@PathVariable problemId: Long, @Valid @RequestBody request: AdminProblemUpdateRequest) =
        BaseResponse.of(adminProblemService.updateProblem(problemId, request))

    @Operation(summary = "문제 삭제", description = "문제를 삭제합니다.")
    @DeleteMapping("/{problemId}")
    fun deleteProblem(@PathVariable problemId: Long) = BaseResponse.of(adminProblemService.deleteProblem(problemId))
}