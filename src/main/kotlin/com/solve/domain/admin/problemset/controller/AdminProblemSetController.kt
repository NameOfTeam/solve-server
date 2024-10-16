package com.solve.domain.admin.problemset.controller

import com.solve.domain.admin.problemset.service.AdminProblemSetService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "관리자: 문제집", description = "Admin: Problem Set")
@RestController
@RequestMapping("/admin/problem-sets")
class AdminProblemSetController(
    private val adminProblemSetService: AdminProblemSetService
) {
    @Operation(summary = "문제집 목록 조회")
    @GetMapping
    fun getProblemSets(@PageableDefault pageable: Pageable) =
        BaseResponse.of(adminProblemSetService.getProblemSets(pageable))

    @Operation(summary = "문제집 상세 조회")
    @GetMapping("/{problemSetId}")
    fun getProblemSet(@PathVariable problemSetId: Long) =
        BaseResponse.of(adminProblemSetService.getProblemSet(problemSetId))
}