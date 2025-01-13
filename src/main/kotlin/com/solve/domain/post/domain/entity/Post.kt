package com.solve.domain.post.domain.entity

import com.solve.domain.post.domain.enums.PostCategory
import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import com.solve.global.common.enums.ProgrammingLanguage
import jakarta.persistence.*

@Entity
@Table(name = "posts")
class Post(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    var content: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    val language: ProgrammingLanguage? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    val category: PostCategory,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    var problem: Problem? = null,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: MutableSet<PostLike> = mutableSetOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<PostComment> = mutableListOf()
): BaseTimeEntity()