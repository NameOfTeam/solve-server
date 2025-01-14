package com.solve.domain.problem.domain.entity

import com.solve.domain.problem.domain.enums.ProblemSubmitState
import com.solve.domain.problem.domain.enums.ProblemSubmitVisibility
import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import com.solve.global.common.enums.ProgrammingLanguage
import jakarta.persistence.*

@Entity
@Table(
    name = "problem_submits",
    indexes = [
        Index(name = "idx_problem_submit_problem", columnList = "problem_id"),
        Index(name = "idx_problem_submit_author", columnList = "author_id"),
        Index(name = "idx_problem_submit_state", columnList = "state"),
        Index(name = "idx_problem_submit_language", columnList = "language")
    ]
)
class ProblemSubmit(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    var state: ProblemSubmitState,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,

    @Column(name = "code", nullable = false, columnDefinition = "TEXT")
    val code: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    val language: ProgrammingLanguage,

    @Column(name = "memory_usage")
    var memoryUsage: Long? = null,

    @Column(name = "time_usage")
    var timeUsage: Long? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    val visibility: ProblemSubmitVisibility,
) : BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProblemSubmit) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}