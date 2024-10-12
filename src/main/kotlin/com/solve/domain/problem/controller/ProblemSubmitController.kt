package com.solve.domain.problem.controller

import com.solve.domain.problem.dto.request.ProblemSubmitRequest
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.service.ProblemSubmitService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*
import java.util.concurrent.CompletableFuture

@Tag(name = "문제 제출", description = "Problem Submit")
@RestController
@RequestMapping("/problems/{problemId}/submit")
class ProblemSubmitController(
    private val problemSubmitService: ProblemSubmitService,
    private val problemRepository: ProblemRepository
) {
    @PostMapping
    fun submitProblem(@PathVariable problemId: Long, @RequestBody request: ProblemSubmitRequest) =
        BaseResponse.of(problemSubmitService.submitProblem(problemId, request))

    @PostMapping("/v2")
    fun submitProblemV2(@PathVariable problemId: Long, @RequestBody request: ProblemSubmitRequest): String {
        CompletableFuture.runAsync {
            val problem = problemRepository.findByIdOrNull(problemId)

            println(problem)
        }

        return "!!"
    }
}