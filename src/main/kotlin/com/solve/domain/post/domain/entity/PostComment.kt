package com.solve.domain.post.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(
    name = "post_comments",
    indexes = [
        Index(name = "idx_post_comment_post", columnList = "post_id"),
        Index(name = "idx_post_comment_author", columnList = "author_id")
    ]
)
class PostComment(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,

    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,

    @OneToMany(
        mappedBy = "comment",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val likes: MutableSet<PostCommentLike> = mutableSetOf(),

    @OneToMany(
        mappedBy = "comment",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val replies: MutableSet<PostCommentReply> = mutableSetOf()
) : BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PostComment) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}