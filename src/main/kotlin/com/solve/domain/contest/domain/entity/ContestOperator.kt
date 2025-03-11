package com.solve.domain.contest.domain.entity

import com.solve.domain.user.domain.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "contest_operators")
class ContestOperator(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    val contest: Contest,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    val user: User,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ContestOperator) return false

        if (contest.id != other.contest.id) return false
        if (user.id != other.user.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = contest.id?.hashCode() ?: 0
        result = 31 * result + (user.id?.hashCode() ?: 0)
        return result
    }
}