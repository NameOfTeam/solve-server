package com.solve.domain.post.domain.entity

import com.solve.domain.user.domain.entity.User
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(
    name = "post_comment_reply_likes",
    indexes = [
        Index(name = "idx_post_comment_reply_like_user", columnList = "user_id"),
        Index(name = "idx_post_comment_reply_like_comment", columnList = "post_comment_reply_id")
    ]
)
class PostCommentReplyLike(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_comment_reply_id", nullable = false)
    val reply: PostCommentReply,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    val user: User
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PostCommentLike) return false

        if (reply.id != other.comment.id) return false
        if (user.id != other.user.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = reply.id?.hashCode() ?: 0
        result = 31 * result + (user.id?.hashCode() ?: 0)
        return result
    }
}