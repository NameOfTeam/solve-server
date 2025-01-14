package com.solve.domain.problem.domain.entity

import com.solve.domain.user.domain.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "problem_idea_likes")
class ProblemIdeaLike(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idea_id", nullable = false)
    val idea: ProblemIdea,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User
)