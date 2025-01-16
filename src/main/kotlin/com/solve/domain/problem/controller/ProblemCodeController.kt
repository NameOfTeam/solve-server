package com.solve.domain.problem.controller

import com.solve.domain.problem.dto.request.ProblemCodeCreateRequest
import com.solve.domain.problem.service.ProblemCodeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "문제 코드 저장", description = "Problem Code")
@RestController
@RequestMapping("/problems/{problemId}/code")
class ProblemCodeController(
    private val problemCodeService: ProblemCodeService
) {
    @Operation(summary = "코드 저장", description = "작성 중인 코드를 저장합니다.")
    @PostMapping
    fun saveCode(@PathVariable problemId: Long, request: ProblemCodeCreateRequest) =
        problemCodeService.saveCode(problemId, request)

    @Operation(summary = "코드 불러오기", description = "저장된 코드를 불러옵니다.")
    @GetMapping
    fun getCode(@PathVariable problemId: Long) = problemCodeService.getCode(problemId)

    @Operation(summary = "코드 초기화", description = "저장된 코드를 삭제합니다.")
    @DeleteMapping
    fun deleteCode(@PathVariable problemId: Long) = problemCodeService.deleteCode(problemId)
}