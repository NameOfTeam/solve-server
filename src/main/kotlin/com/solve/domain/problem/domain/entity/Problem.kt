package com.solve.domain.problem.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import com.solve.global.common.enums.Tier
import jakarta.persistence.*
import org.hibernate.annotations.BatchSize

@Entity
@Table(
    name = "problems",
    indexes = [
        Index(name = "idx_problem_author", columnList = "author_id"),
        Index(name = "idx_problem_tier", columnList = "tier")
    ]
)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,
) : BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Problem) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}