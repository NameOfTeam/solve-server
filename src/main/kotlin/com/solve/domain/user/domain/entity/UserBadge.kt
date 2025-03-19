package com.solve.domain.user.domain.entity

import com.solve.domain.badge.domain.entity.Badge
import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "user_badges")
class UserBadge(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    val badge: Badge
): BaseTimeEntity()