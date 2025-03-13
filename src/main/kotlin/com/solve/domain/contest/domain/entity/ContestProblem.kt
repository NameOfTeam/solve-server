package com.solve.domain.contest.domain.entity

import com.solve.domain.problem.domain.entity.Problem
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "contest_problems")
class ContestProblem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    val contest: Contest,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,

    val order: Int,
    val score: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ContestProblem) return false

        if (contest.id != other.contest.id) return false
        if (problem.id != other.problem.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contest.id?.hashCode() ?: 0
        result = 31 * result + (problem.id?.hashCode() ?: 0)
        return result
    }
}