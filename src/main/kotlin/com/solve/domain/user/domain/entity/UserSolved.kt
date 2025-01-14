package com.solve.domain.user.domain.entity

import com.solve.domain.problem.domain.entity.Problem
import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.time.LocalDate

@Entity
@Table(
    name = "user_solved",
    indexes = [
        Index(name = "idx_user_solved_user", columnList = "user_id"),
        Index(name = "idx_user_solved_problem", columnList = "problem_id"),
        Index(name = "idx_user_solved_date", columnList = "date")
    ]
)
class UserSolved(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    @OnDelete(action = OnDeleteAction.CASCADE)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val problem: Problem,

    @Column(name = "date", nullable = false)
    val date: LocalDate
) : BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserSolved) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}