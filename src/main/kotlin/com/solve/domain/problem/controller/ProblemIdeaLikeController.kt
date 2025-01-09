package com.solve.domain.problem.controller

import com.solve.domain.problem.service.ProblemIdeaLikeService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Problem Idea Like")
@RestController
@RequestMapping("/problems/{problemId}/ideas/{ideaId}/like")
class ProblemIdeaLikeController(
    private val problemIdeaLikeService: ProblemIdeaLikeService
) {
    @Operation(summary = "문제 아이디어 좋아요 토글")
    @PostMapping
    fun toggleProblemIdeaLike(@PathVariable problemId: Long, @PathVariable ideaId: Long) =
        BaseResponse.of(problemIdeaLikeService.toggleProblemIdeaLike(problemId, ideaId), 201)
}