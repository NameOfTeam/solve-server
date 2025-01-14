package com.solve.domain.post.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "post_comments")
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

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    val likes: List<PostCommentLike> = mutableListOf()
): BaseTimeEntity()