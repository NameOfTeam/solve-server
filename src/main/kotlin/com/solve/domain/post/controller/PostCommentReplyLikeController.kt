package com.solve.domain.post.controller

import com.solve.domain.post.service.PostCommentReplyLikeService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "게시글 댓글 답글 좋아요")
@RestController
@RequestMapping("/posts/{postId}/comments/{commentId}/replies/{replyId}/likes")
class PostCommentReplyLikeController(
    private val postCommentReplyLikeService: PostCommentReplyLikeService
) {
    @Operation(summary = "게시글 댓글 답글 좋아요")
    @PostMapping
    fun likeCommentReply(@PathVariable postId: Long, @PathVariable commentId: Long, @PathVariable replyId: Long) =
        BaseResponse.of(postCommentReplyLikeService.likeCommentReply(postId, commentId, replyId))

    @Operation(summary = "게시글 댓글 답글 좋아요 취소")
    @DeleteMapping
    fun unlikeCommentReply(@PathVariable postId: Long, @PathVariable commentId: Long, @PathVariable replyId: Long) =
        BaseResponse.of(postCommentReplyLikeService.unlikeCommentReply(postId, commentId, replyId))
}