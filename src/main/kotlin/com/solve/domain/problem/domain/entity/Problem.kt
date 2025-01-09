package com.solve.domain.problem.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import com.solve.global.common.enums.Tier
import jakarta.persistence.*

@Entity
@Table(name = "problems")
class Problem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "input", nullable = false, columnDefinition = "TEXT")
    var input: String,

    @Column(name = "output", nullable = false, columnDefinition = "TEXT")
    var output: String,

    @Column(name = "memory_limit", nullable = false)
    var memoryLimit: Long,

    @Column(name = "time_limit", nullable = false)
    var timeLimit: Double,

    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false)
    val tier: Tier,

    @OneToMany(mappedBy = "problem", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    val examples: MutableSet<ProblemExample> = mutableSetOf(),

    @OneToMany(mappedBy = "problem", fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    val testCases: MutableSet<ProblemTestCase> = mutableSetOf(),

    @OneToMany(mappedBy = "problem", cascade = [CascadeType.ALL], orphanRemoval = true)
    val contributors: MutableSet<ProblemContributor> = mutableSetOf(),

    @OneToMany(mappedBy = "problem", cascade = [CascadeType.ALL], orphanRemoval = true)
    val submits: MutableSet<ProblemSubmit> = mutableSetOf(),

    @OneToMany(mappedBy = "problem", cascade = [CascadeType.ALL], orphanRemoval = true)
    val ideas: MutableSet<ProblemIdea> = mutableSetOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,
) : BaseTimeEntity()