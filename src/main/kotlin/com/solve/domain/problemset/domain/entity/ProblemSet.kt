package com.solve.domain.problemset.domain.entity

import com.solve.domain.user.domain.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "problem_sets")
class ProblemSet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @OneToMany(mappedBy = "problemSet", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val problems: MutableList<ProblemSetProblem> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User
)