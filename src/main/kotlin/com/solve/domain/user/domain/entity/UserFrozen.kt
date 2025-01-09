package com.solve.domain.user.domain.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "user_frozen")
class UserFrozen(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "date", nullable = false)
    val date: LocalDate
) {
}