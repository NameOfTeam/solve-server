package com.solve.domain.contest.controller

import com.solve.domain.contest.domain.enums.ContestSearchState
import com.solve.domain.contest.service.ContestSearchService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "대회 검색", description = "Contest Search")
@RestController
@RequestMapping("/contests/search")
class ContestSearchController(
    private val contestSearchService: ContestSearchService
) {
    @Operation(summary = "대회 검색")
    @GetMapping
    fun searchContest(
        @RequestParam(required = false, defaultValue = "") query: String,
        @RequestParam(required = false) state: ContestSearchState?,
        @PageableDefault pageable: Pageable
    ) = BaseResponse.of(contestSearchService.searchContest(query, state, pageable))
}