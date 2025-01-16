package com.solve.domain.problem.controller

import com.solve.domain.problem.dto.request.RunCodeRequest
import com.solve.domain.problem.service.impl.ProblemRunService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/problems/runs")
class ProblemRunController(
    private val problemRunService: ProblemRunService
) {
    @PostMapping
    fun runCode(@RequestBody request: RunCodeRequest) : ResponseEntity<ExecuteResponse>{
        val runId = problemRunService.initializeRun(request)
        return ResponseEntity.ok(ExecuteResponse(
            runId = runId,
            wsUrl = "/ws/run/$runId"  // WebSocket 연결을 위한 URL 반환
        ))
    }
}

data class ExecuteResponse(
    val runId: String,
    val wsUrl: String
)