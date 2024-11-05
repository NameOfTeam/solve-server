package com.solve.domain.user.controller

import com.solve.domain.user.service.UserMeSolvedService
import com.solve.global.common.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "유저 내가 푼 문제")
@RestController
@RequestMapping("/users/me/solved")
class UserMeSolvedController(
    private val userMeSolvedService: UserMeSolvedService
) {
    @Operation(summary = "내가 푼 문제 조회")
    @GetMapping
    fun getSolvedProblems() = BaseResponse.of(userMeSolvedService.getSolvedProblems())
}