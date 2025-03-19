package com.solve.domain.user.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "user_links")
class UserLink(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "link", nullable = false)
    val link: String,
)