package com.solve.domain.post.controller

import com.solve.domain.post.dto.request.PostCommentCreateRequest
import com.solve.domain.post.dto.request.PostCommentUpdateRequest
import com.solve.domain.post.service.PostCommentService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "게시글 댓글", description = "Post Comment")
@RestController
@RequestMapping("/posts/{postId}/comments")
class PostCommentController(
    private val postCommentService: PostCommentService
) {
    @Operation(summary = "게시글 댓글 조회")
    @GetMapping
    fun getComments(
        @PathVariable postId: Long,
        @RequestParam(required = false) cursorId: Long?,
        @RequestParam(required = false, defaultValue = "10") size: Int
    ) = BaseResponse.of(postCommentService.getComments(postId, cursorId, size))

    @Operation(summary = "게시글 댓글 생성")
    @PostMapping
    fun createComment(@PathVariable postId: Long, @RequestBody request: PostCommentCreateRequest) =
        BaseResponse.of(postCommentService.createComment(postId, request))

    @Operation(summary = "게시글 댓글 수정")
    @PatchMapping("/{commentId}")
    fun updateComment(
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @RequestBody request: PostCommentUpdateRequest
    ) = BaseResponse.of(postCommentService.updateComment(postId, commentId, request))

    @Operation(summary = "게시글 댓글 삭제")
    @DeleteMapping("/{commentId}")
    fun deleteComment(@PathVariable postId: Long, @PathVariable commentId: Long) =
        BaseResponse.of(postCommentService.deleteComment(postId, commentId))
}