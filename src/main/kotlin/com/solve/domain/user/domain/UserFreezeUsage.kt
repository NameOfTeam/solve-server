package com.solve.domain.user.domain

import com.solve.domain.user.domain.entity.User
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "user_freeze_usages")
class UserFreezeUsage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "freeze_date", nullable = false)
    val freezeDate: LocalDate
) {
}