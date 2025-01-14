package com.solve.domain.problem.controller

import com.solve.domain.problem.dto.request.ProblemCodeCreateRequest
import com.solve.domain.problem.dto.request.ProblemCodeUpdateRequest
import com.solve.domain.problem.service.ProblemCodeService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "코드 저장", description = "Problem Code")
@RestController
@RequestMapping("problems/code")
class ProblemCodeController(
    private val problemCodeService: ProblemCodeService
) {
    @Operation(summary = "코드 저장", description = "작성중인 코드를 저징합니다.")
    @PostMapping("/{problemId}")
    fun saveCode(@PathVariable problemId: Long, @RequestBody request: ProblemCodeCreateRequest) =
        BaseResponse.of(problemCodeService.saveCode(problemId, request))

    @Operation(summary = "코드 조회", description = "저장된 코드를 조회합니다.")
    @GetMapping("/{problemCodeId}")
    fun getCode(@PathVariable problemCodeId: Long) = BaseResponse.of(problemCodeService.getCode(problemCodeId))

    @Operation(summary = "코드 수정", description = "저장된 코드를 수정합니다.")
    @PatchMapping("/{problemCodeId}")
    fun updateCode(@PathVariable problemCodeId: Long, @RequestBody request: ProblemCodeUpdateRequest) =
        BaseResponse.of(problemCodeService.updateCode(problemCodeId, request))

    @Operation(summary = "코드 초기화", description = "저장된 코드를 삭제합니다.")
    @DeleteMapping("/{problemCodeId}")
    fun deleteCode(@PathVariable problemCodeId: Long) = BaseResponse.of(problemCodeService.deleteCode(problemCodeId))
}