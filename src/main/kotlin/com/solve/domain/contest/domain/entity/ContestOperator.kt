package com.solve.domain.contest.domain.entity

import com.solve.domain.user.domain.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "contest_operators")
class ContestOperator(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    val contest: Contest,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
)