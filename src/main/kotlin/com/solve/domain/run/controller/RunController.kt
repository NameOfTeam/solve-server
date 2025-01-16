package com.solve.domain.run.controller

import com.solve.domain.run.dto.request.RunCodeRequest
import com.solve.domain.run.dto.response.RunResponse
import com.solve.domain.run.service.RunService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@Tag(name = "코드 실행", description = "run code")
@RestController
@RequestMapping("/runs")
class RunController(
    private val problemRunService: RunService
) {
    @Operation(summary = "코드 실행", description = "코드를 실행합니다.")
    @PostMapping
    fun runCode(@RequestBody request: RunCodeRequest) : ResponseEntity<RunResponse>{
        val runId = problemRunService.initializeRun(request)
        return ResponseEntity.ok(RunResponse(runId = runId))
    }
}