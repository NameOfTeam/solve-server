package com.solve.domain.submit.controller

import com.solve.domain.submit.domain.enums.SubmitState
import com.solve.domain.submit.dto.request.SubmitRequest
import com.solve.domain.submit.service.SubmitService
import com.solve.global.common.dto.BaseResponse
import com.solve.global.common.enums.ProgrammingLanguage
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "문제 제출", description = "Problem Submit")
@RestController
@RequestMapping("/submits")
class SubmitController(
    private val submitService: SubmitService
) {
    @Operation(summary = "문제 제출")
    @PostMapping
    fun submitProblem(@RequestParam problemId: Long, @RequestBody request: SubmitRequest) =
        BaseResponse.of(submitService.submitProblem(problemId, request))

    @Operation(summary = "제출 검색")
    @GetMapping("/search")
    fun searchSubmit(
        @RequestParam(required = false) problemId: Long?,
        @RequestParam(required = false) username: String?,
        @RequestParam(required = false) state: SubmitState?,
        @RequestParam(required = false) language: ProgrammingLanguage?,
        @RequestParam(required = false) cursorId: Long?,
        @RequestParam(required = false, defaultValue = "10") size: Int
    ) = BaseResponse.of(submitService.searchSubmit(problemId, username, state, language, cursorId, size))

    @Operation(summary = "내 제출 조회")
    @GetMapping("/my")
    fun getMySubmits(
        @RequestParam problemId: Long,
        @RequestParam(required = false) cursorId: Long,
        @RequestParam(required = false, defaultValue = "10") size: Int
    ) = BaseResponse.of(submitService.getMySubmits(problemId, cursorId, size))
}