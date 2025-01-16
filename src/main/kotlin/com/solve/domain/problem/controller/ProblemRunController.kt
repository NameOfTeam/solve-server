package com.solve.domain.problem.controller

import com.solve.domain.problem.dto.request.RunCodeRequest
import com.solve.domain.problem.dto.response.ProblemRunResponse
import com.solve.domain.problem.service.impl.ProblemRunService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "코드 실행", description = "코드를 실행합니다.")
@RestController
@RequestMapping("/problems/runs")
class ProblemRunController(
    private val problemRunService: ProblemRunService
) {
    @Operation(summary = "코드 실행", description = "코드를 실행합니다.")
    @PostMapping
    fun runCode(@RequestBody request: RunCodeRequest) : ResponseEntity<ProblemRunResponse>{
        val runId = problemRunService.initializeRun(request)
        return ResponseEntity.ok(ProblemRunResponse(runId = runId))
    }
}