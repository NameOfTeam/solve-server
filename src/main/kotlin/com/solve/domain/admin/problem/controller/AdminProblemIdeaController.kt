package com.solve.domain.admin.problem.controller

import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "관리자: 문제 아이디어", description = "Admin: Problem Idea")
@RestController
@RequestMapping("/admin/problems/{problemId}/ideas")
class AdminProblemIdeaController {
    @Operation(summary = "문제 아이디어 목록 조회", description = "문제의 아이디어 목록을 조회합니다.")
    fun getProblemIdeas(@PathVariable problemId: Long) = BaseResponse.of("getProblemIdeas")
}