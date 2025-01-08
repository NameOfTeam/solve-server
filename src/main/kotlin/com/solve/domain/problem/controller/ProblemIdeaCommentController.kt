package com.solve.domain.problem.controller

import com.solve.domain.problem.dto.request.ProblemIdeaCommentCreateRequest
import com.solve.domain.problem.dto.request.ProblemIdeaCommentUpdateRequest
import com.solve.domain.problem.service.ProblemIdeaCommentService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "문제 아이디어 댓글", description = "Problem Idea Comment")
@RestController
@RequestMapping("/problems/{problemId}/ideas/{ideaId}/comments")
class ProblemIdeaCommentController(
    private val problemIdeaCommentService: ProblemIdeaCommentService
) {
    @Operation(summary = "문제 아이디어 댓글 목록 조회", description = "문제 아이디어의 댓글 목록을 조회합니다.")
    @GetMapping
    fun getProblemIdeaComments(@PathVariable problemId: Long, @PathVariable ideaId: Long) =
        BaseResponse.of(problemIdeaCommentService.getProblemIdeaComments(problemId, ideaId))

    @Operation(summary = "문제 아이디어 댓글 생성", description = "문제 아이디어의 댓글을 생성합니다.")
    @PostMapping
    fun createProblemIdeaComment(
        @PathVariable problemId: Long,
        @PathVariable ideaId: Long,
        @RequestBody request: ProblemIdeaCommentCreateRequest
    ) = BaseResponse.of(problemIdeaCommentService.createProblemIdeaComment(problemId, ideaId, request), 201)

    @Operation(summary = "문제 아이디어 댓글 수정", description = "문제 아이디어의 댓글을 수정합니다.")
    @PatchMapping("/{commentId}")
    fun updateProblemIdeaComment(
        @PathVariable problemId: Long,
        @PathVariable ideaId: Long,
        @PathVariable commentId: Long,
        @RequestBody request: ProblemIdeaCommentUpdateRequest
    ) = BaseResponse.of(problemIdeaCommentService.updateProblemIdeaComment(problemId, ideaId, commentId, request))

    @Operation(summary = "문제 아이디어 댓글 삭제", description = "문제 아이디어의 댓글을 삭제합니다.")
    @DeleteMapping("/{commentId}")
    fun deleteProblemIdeaComment(
        @PathVariable problemId: Long,
        @PathVariable ideaId: Long,
        @PathVariable commentId: Long
    ) = BaseResponse.of(problemIdeaCommentService.deleteProblemIdeaComment(problemId, ideaId, commentId))
}