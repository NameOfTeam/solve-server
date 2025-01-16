package com.solve.domain.post.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class PostCommentError(override val status: HttpStatus, override val message: String) : CustomError {
    POST_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글 댓글을 찾을 수 없습니다. (id: %s)"),
    POST_COMMENT_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "게시글 댓글에 권한이 없습니다. (id: %s)"),
    POST_COMMENT_CURSOR_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글 댓글을 찾을 수 없습니다. (cursor: %s)"),
}