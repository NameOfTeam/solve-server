package com.solve.domain.post.controller

import com.solve.domain.post.service.PostCommentLikeService
import com.solve.global.common.dto.BaseResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "게시글 댓글 좋아요")
@RestController
@RequestMapping("/posts/{postId}/comments/{commentId}/likes")
class PostCommentLikeController(
    private val postCommentLikeService: PostCommentLikeService
) {
    @Operation(summary = "게시글 댓글 좋아요")
    @PostMapping
    fun likeComment(@PathVariable postId: Long, @PathVariable commentId: Long) = BaseResponse.of(postCommentLikeService.likeComment(postId, commentId), 201)

    @Operation(summary = "게시글 댓글 좋아요 취소")
    @DeleteMapping
    fun unlikeComment(@PathVariable postId: Long, @PathVariable commentId: Long) = BaseResponse.of(postCommentLikeService.unlikeComment(postId, commentId), 204)
}