package com.solve.domain.user.controller

import com.solve.domain.user.service.UserSearchService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "사용자 검색", description = "User Search")
@RestController
@RequestMapping("/users/search")
class UserSearchController(
    private val userSearchService: UserSearchService
) {
    @Operation(summary = "사용자 검색", description = "사용자를 검색합니다.")
    @GetMapping
    fun searchUser(
        @RequestParam(required = false, defaultValue = "") query: String,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "10") size: Int
    ) = BaseResponse.of(userSearchService.searchUser(query, PageRequest.of(page, size)))
}