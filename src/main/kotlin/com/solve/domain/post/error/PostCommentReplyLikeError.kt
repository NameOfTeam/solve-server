package com.solve.domain.post.error

import com.solve.global.error.CustomError
import org.springframework.http.HttpStatus

enum class PostCommentReplyLikeError(override val status: HttpStatus, override val message: String): CustomError {
    POST_COMMENT_REPLY_ALREADY_LIKED(HttpStatus.BAD_REQUEST, "이미 좋아요를 누른 답글입니다. (id: %s)"),
    POST_COMMENT_REPLY_NOT_LIKED(HttpStatus.BAD_REQUEST, "좋아요를 누르지 않은 답글입니다. (id: %s)"),
}