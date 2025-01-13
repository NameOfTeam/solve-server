package com.solve.domain.problem.domain.entity

import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(
    name = "problem_examples",
    indexes = [
        Index(name = "idx_problem_example_problem", columnList = "problem_id")
    ]
)
class ProblemExample(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "input", nullable = false, columnDefinition = "TEXT")
    var input: String,

    @Column(name = "output", nullable = false, columnDefinition = "TEXT")
    var output: String,

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val problem: Problem
): BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProblemExample) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}