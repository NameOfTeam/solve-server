package com.solve.domain.user.domain.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(
    name = "user_frozen",
    indexes = [
        Index(name = "idx_user_frozen_user", columnList = "user_id"),
        Index(name = "idx_user_frozen_date", columnList = "date")
    ]
)
class UserFrozen(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    val user: User,

    @Column(name = "date", nullable = false)
    val date: LocalDate
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserFrozen) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}