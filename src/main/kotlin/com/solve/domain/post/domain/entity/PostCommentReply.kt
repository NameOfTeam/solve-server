package com.solve.domain.post.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(
    name = "post_comment_replies",
    indexes = [
        Index(name = "idx_post_comment_reply_post", columnList = "post_id"),
        Index(name = "idx_post_comment_reply_author", columnList = "author_id")
    ]
)
class PostCommentReply(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    val post: Post,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = true)
    val comment: PostComment,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_id")
    val reply: PostCommentReply? = null,

    @OneToMany(
        mappedBy = "reply",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val likes: MutableSet<PostReplyLike> = mutableSetOf()
): BaseTimeEntity()