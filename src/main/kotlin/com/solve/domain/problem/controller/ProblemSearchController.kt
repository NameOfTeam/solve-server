package com.solve.domain.problem.controller

import com.solve.domain.problem.domain.enums.ProblemSearchOrder
import com.solve.domain.problem.domain.enums.ProblemSearchState
import com.solve.domain.problem.service.ProblemSearchService
import com.solve.global.common.dto.BaseResponse
import com.solve.global.common.enums.Tier
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "문제 검색", description = "Problem Search")
@RestController
@RequestMapping("/problems/search")
class ProblemSearchController(
    private val problemSearchService: ProblemSearchService
) {
    @Operation(summary = "검색")
    @GetMapping
    fun searchProblem(
        @RequestParam(defaultValue = "", required = false) query: String,
        @RequestParam(defaultValue = "", required = false) states: List<ProblemSearchState>,
        @RequestParam(defaultValue = "", required = false) tiers: List<Tier>,
        @RequestParam(defaultValue = "LATEST", required = false) order: ProblemSearchOrder,
        @PageableDefault pageable: Pageable
    ) = BaseResponse.of(problemSearchService.searchProblem(query, states, tiers, order, pageable))
}