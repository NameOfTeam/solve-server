package com.solve.domain.problem.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.global.common.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "problem_ideas")
class ProblemIdea(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @OneToMany(mappedBy = "idea", cascade = [CascadeType.ALL], orphanRemoval = true)
    val comments: MutableList<ProblemIdeaComment> = mutableListOf(),

    @OneToMany(mappedBy = "idea", cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: MutableList<ProblemIdeaLike> = mutableListOf()
) : BaseTimeEntity() {
    fun addComment(comment: ProblemIdeaComment) {
        comments.add(comment)
    }

    fun addLike(like: ProblemIdeaLike) {
        likes.add(like)
    }

    fun removeLike(user: User) {
        likes.removeIf { it.author == user }
    }

    val rootComments: List<ProblemIdeaComment>
        get() = comments.filter { it.parent == null }
}