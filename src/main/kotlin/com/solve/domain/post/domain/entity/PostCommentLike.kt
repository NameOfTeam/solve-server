package com.solve.domain.post.domain.entity

import com.solve.domain.user.domain.entity.User
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.io.Serializable

@Entity
@Table(
    name = "post_comment_likes",
    indexes = [
        Index(name = "idx_post_comment_like_user", columnList = "user_id"),
        Index(name = "idx_post_comment_like_comment", columnList = "post_comment_id")
    ]
)
@IdClass(PostCommentLikeId::class)
class PostCommentLike(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_comment_id", nullable = false)
    val comment: PostComment,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    val user: User
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PostCommentLike) return false

        if (comment.id != other.comment.id) return false
        if (user.id != other.user.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = comment.id?.hashCode() ?: 0
        result = 31 * result + (user.id?.hashCode() ?: 0)
        return result
    }
}

class PostCommentLikeId(
    val comment: Long? = null,
    val user: Long? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PostCommentLikeId) return false

        if (comment != other.comment) return false
        if (user != other.user) return false

        return true
    }

    override fun hashCode(): Int {
        var result = comment?.hashCode() ?: 0
        result = 31 * result + (user?.hashCode() ?: 0)
        return result
    }
}