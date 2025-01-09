package com.solve.domain.problem.controller

import com.solve.domain.problem.dto.request.ProblemSubmitRequest
import com.solve.domain.problem.service.ProblemSubmitService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "문제 제출", description = "Problem Submit")
@RestController
@RequestMapping("/problems/{problemId}/submit")
class ProblemSubmitController(
    private val problemSubmitService: ProblemSubmitService
) {
    @PostMapping
    fun submitProblem(@PathVariable problemId: Long, @RequestBody request: ProblemSubmitRequest) =
        BaseResponse.of(problemSubmitService.submitProblem(problemId, request))
}