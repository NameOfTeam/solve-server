package com.solve.domain.contest.domain.entity

import com.solve.domain.problem.domain.entity.Problem
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "contest_problems")
@IdClass(ContestProblemId::class)
class ContestProblem(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    val contest: Contest,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,
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

class ContestProblemId(
    val contest: Long? = null,
    val problem: Long? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ContestProblemId) return false

        if (contest != other.contest) return false
        if (problem != other.problem) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contest?.hashCode() ?: 0
        result = 31 * result + (problem?.hashCode() ?: 0)
        return result
    }
}