package com.solve.domain.run.controller

import com.solve.domain.run.dto.request.RunCodeRequest
import com.solve.domain.run.service.RunService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "코드 실행", description = "Run Code")
@RestController
@RequestMapping("/runs")
class RunController(
    private val runService: RunService
) {
    @Operation(summary = "코드 실행", description = "코드를 실행합니다.")
    @PostMapping
    fun runCode(@RequestBody request: RunCodeRequest) = BaseResponse.of(runService.runCode(request))
}