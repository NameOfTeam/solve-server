package com.solve.domain.post.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class PostCommentReplyError(override val status: HttpStatus, override val message: String): CustomError {
    POST_COMMENT_REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글 답글을 찾을 수 없습니다. (id: %s)"),
    POST_COMMENT_REPLY_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "댓글 답글 권한이 없습니다.")
}