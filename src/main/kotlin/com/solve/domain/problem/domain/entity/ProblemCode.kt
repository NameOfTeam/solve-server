package com.solve.domain.problem.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import com.solve.global.common.enums.ProgrammingLanguage
import jakarta.persistence.*

@Entity
@Table(
    name = "problem_codes",
    indexes = [
        Index(name = "idx_problem_code_user", columnList = "user_id"),
        Index(name = "idx_problem_code_problem", columnList = "problem_id"),
        Index(name = "idx_problem_code_language", columnList = "language")
    ],
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_problem_code_user_language",
            columnNames = ["problem_id", "user_id", "language"]
        )
    ]
)
class ProblemCode(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    var code: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var language: ProgrammingLanguage,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    val user: User,
) : BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProblemCode) return false

        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}