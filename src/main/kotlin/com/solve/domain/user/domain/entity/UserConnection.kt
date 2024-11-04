package com.solve.domain.user.domain.entity

import com.solve.domain.user.domain.enums.UserConnectionType
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_connections")
class UserConnection(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: UserConnectionType,

    @Column(name = "value", nullable = false)
    val value: String
)