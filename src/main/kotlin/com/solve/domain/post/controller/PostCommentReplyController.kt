package com.solve.domain.post.controller

import com.solve.domain.post.dto.request.PostCommentReplyCreateRequest
import com.solve.domain.post.dto.request.PostCommentReplyUpdateRequest
import com.solve.domain.post.service.PostCommentReplyService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "게시글 댓글 답글", description = "Post Comment Reply")
@RestController
@RequestMapping("/posts/{postId}/comments/{commentId}/replies")
class PostCommentReplyController(
    private val postCommentReplyService: PostCommentReplyService
) {
    @Operation(summary = "게시글 댓글 답글 조회")
    @GetMapping
    fun getReplies(
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @RequestParam(required = false) cursorId: Long?,
        @RequestParam(required = false, defaultValue = "10") size: Int
    ) = BaseResponse.of(postCommentReplyService.getReplies(postId, commentId, cursorId, size))

    @Operation(summary = "게시글 댓글 답글 생성")
    @PostMapping
    fun createReply(@PathVariable postId: Long, @PathVariable commentId: Long, @RequestBody request: PostCommentReplyCreateRequest) = BaseResponse.of(postCommentReplyService.createReply(postId, commentId, request))

    @Operation(summary = "게시글 댓글 답글 수정")
    @PatchMapping("/{replyId}")
    fun updateReply(@PathVariable postId: Long, @PathVariable commentId: Long, @PathVariable replyId: Long, @RequestBody request: PostCommentReplyUpdateRequest) = BaseResponse.of(postCommentReplyService.updateReply(postId, commentId, replyId, request))

    @Operation(summary = "게시글 댓글 답글 삭제")
    @DeleteMapping("/{replyId}")
    fun deleteReply(@PathVariable postId: Long, @PathVariable commentId: Long, @PathVariable replyId: Long) = BaseResponse.of(postCommentReplyService.deleteReply(postId, commentId, replyId))
}