package com.solve.domain.post.controller

import com.solve.domain.post.dto.request.PostCreateRequest
import com.solve.domain.post.dto.request.PostUpdateRequest
import com.solve.domain.post.service.PostService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "게시글", description = "Post")
@RestController
@RequestMapping("/posts")
class PostController(
    private val postService: PostService
) {
    @Operation(summary = "게시글 생성")
    @PostMapping
    fun createPost(@RequestBody request: PostCreateRequest) = BaseResponse.of(postService.createPost(request))

    @Operation(summary = "게시글 조회")
    @GetMapping("/{postId}")
    fun getPost(@PathVariable postId: Long) = BaseResponse.of(postService.getPost(postId))

    @Operation(summary = "게시글 수정")
    @PatchMapping("/{postId}")
    fun updatePost(@PathVariable postId: Long, @RequestBody request: PostUpdateRequest) =
        BaseResponse.of(postService.updatePost(postId, request))

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    fun deletePost(@PathVariable postId: Long) = BaseResponse.of(postService.deletePost(postId))
}