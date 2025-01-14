package com.solve.domain.problem.controller

import com.solve.domain.problem.dto.request.ProblemCodeCreateRequest
import com.solve.domain.problem.dto.request.ProblemCodeUpdateRequest
import com.solve.domain.problem.service.ProblemCodeService
import com.solve.global.common.dto.BaseResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("problems/code")
class ProblemCodeController(
    private val problemCodeService: ProblemCodeService
) {
    @PostMapping("/{problemId}")
    fun saveCode(@PathVariable problemId: Long, @RequestBody request: ProblemCodeCreateRequest) =
        BaseResponse.of(problemCodeService.saveCode(problemId, request))

    @GetMapping("/{problemCodeId}")
    fun getCode(@PathVariable problemCodeId: Long) = BaseResponse.of(problemCodeService.getCode(problemCodeId))

    @PatchMapping("/{problemCodeId}")
    fun updateCode(@PathVariable problemCodeId: Long, @RequestBody request: ProblemCodeUpdateRequest) =
        BaseResponse.of(problemCodeService.updateCode(problemCodeId, request))

    @DeleteMapping("/{problemCodeId}")
    fun deleteCode(@PathVariable problemCodeId: Long) = BaseResponse.of(problemCodeService.deleteCode(problemCodeId))
}