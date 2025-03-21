package com.solve.domain.post.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(
    name = "post_likes",
    indexes = [
        Index(name = "idx_post_like_user", columnList = "user_id"),
        Index(name = "idx_post_like_post", columnList = "post_id")
    ]
)
class PostLike(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    val user: User
) : BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PostLike) return false

        if (post.id != other.post.id) return false
        if (user.id != other.user.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = post.id?.hashCode() ?: 0
        result = 31 * result + (user.id?.hashCode() ?: 0)
        return result
    }
}