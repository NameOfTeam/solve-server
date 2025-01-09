package com.solve.domain.problem.controller

import com.solve.domain.problem.dto.request.ProblemIdeaCreateRequest
import com.solve.domain.problem.dto.request.ProblemIdeaUpdateRequest
import com.solve.domain.problem.service.ProblemIdeaService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "문제 아이디어", description = "Problem Idea")
@RestController
@RequestMapping("/problems/{problemId}/ideas")
class ProblemIdeaController(
    private val problemIdeaService: ProblemIdeaService
) {
    @Operation(summary = "문제 아이디어 목록 조회")
    @GetMapping
    fun getMyProblemIdeas(@PathVariable problemId: Long) =
        BaseResponse.of(problemIdeaService.getProblemIdeas(problemId))

    @Operation(summary = "문제 아이디어 상세 조회")
    @GetMapping("/{ideaId}")
    fun getProblemIdea(@PathVariable problemId: Long, @PathVariable ideaId: Long) =
        BaseResponse.of(problemIdeaService.getProblemIdea(problemId, ideaId))

    @Operation(summary = "문제 아이디어 생성")
    @PostMapping
    fun createProblemIdea(@PathVariable problemId: Long, @RequestBody request: ProblemIdeaCreateRequest) =
        BaseResponse.of(problemIdeaService.createProblemIdea(problemId, request), 201)

    @Operation(summary = "문제 아이디어 수정")
    @PatchMapping("/{ideaId}")
    fun updateProblemIdea(
        @PathVariable problemId: Long,
        @PathVariable ideaId: Long,
        @RequestBody request: ProblemIdeaUpdateRequest
    ) = BaseResponse.of(problemIdeaService.updateProblemIdea(problemId, ideaId, request))

    @Operation(summary = "문제 아이디어 삭제")
    @DeleteMapping("/{ideaId}")
    fun deleteProblemIdea(@PathVariable problemId: Long, @PathVariable ideaId: Long) =
        BaseResponse.of(problemIdeaService.deleteProblemIdea(problemId, ideaId))
}