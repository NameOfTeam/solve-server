package com.solve.domain.problem.domain.entity

import com.solve.domain.problem.domain.enums.ProblemSubmitLanguage
import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.domain.enums.ProblemSubmitVisibility
import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "problem_submits")
class ProblemSubmit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    var state: ProblemSubmitState,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    val problem: Problem,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    val author: User,

    @Column(name = "code", nullable = false, columnDefinition = "TEXT")
    val code: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    val language: ProblemSubmitLanguage,

    @Column(name = "memory_usage")
    var memoryUsage: Long? = null,

    @Column(name = "time_usage")
    var timeUsage: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    val visibility: ProblemSubmitVisibility,
) : BaseTimeEntity()