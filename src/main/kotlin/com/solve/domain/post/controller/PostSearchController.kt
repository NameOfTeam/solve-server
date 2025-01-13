package com.solve.domain.post.controller

import com.solve.domain.post.domain.enums.PostCategory
import com.solve.domain.post.service.PostSearchService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "게시글 검색", description = "Post Search")
@RestController
@RequestMapping("/posts/search")
class PostSearchController(
    private val postSearchService: PostSearchService
) {
    @Operation(summary = "게시글 검색")
    @GetMapping
    fun searchPost(
        @RequestParam(required = false, defaultValue = "") query: String,
        @RequestParam(required = false) category: PostCategory?,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "10") size: Int
    ) = BaseResponse.of(postSearchService.searchPost(query, category, PageRequest.of(page, size)))
}