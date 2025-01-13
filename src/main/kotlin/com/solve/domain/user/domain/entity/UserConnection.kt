package com.solve.domain.user.domain.entity

import com.solve.domain.user.domain.enums.UserConnectionType
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*

@Entity
@Table(
    name = "user_connections",
    indexes = [
        Index(name = "idx_user_connection_user", columnList = "user_id"),
        Index(name = "idx_user_connection_type", columnList = "type"),
        Index(name = "idx_user_connection_value", columnList = "value")
    ]
)
class UserConnection(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    @OnDelete(action = OnDeleteAction.CASCADE)
    val user: User,

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    val type: UserConnectionType,

    @Column(name = "value", nullable = false)
    var value: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserConnection) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}