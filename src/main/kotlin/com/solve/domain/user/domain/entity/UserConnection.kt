package com.solve.domain.user.domain.entity

import com.solve.domain.user.domain.enums.UserConnectionType
import jakarta.persistence.*

@Entity
@Table(name = "user_connections")
class UserConnection(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val connectionType: UserConnectionType,

    @Column(name = "url", nullable = false)
    val url: String
)