package com.solve.domain.workbook.domain.entity

import com.solve.domain.user.domain.entity.User
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(
    name = "workbook_likes",
    indexes = [
        Index(name = "idx_workbook_like_user", columnList = "user_id"),
        Index(name = "idx_workbook_like_workbook", columnList = "workbook_id")
    ]
)
@IdClass(WorkbookLikeId::class)
class WorkbookLike(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id", nullable = false)
    val workbook: Workbook,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    val user: User
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WorkbookLike) return false

        if (workbook.id != other.workbook.id) return false
        if (user.id != other.user.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = workbook.id?.hashCode() ?: 0
        result = 31 * result + (user.id?.hashCode() ?: 0)
        return result
    }
}

class WorkbookLikeId(
    val workbook: Long? = null,
    val user: Long? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WorkbookLikeId) return false

        if (workbook != other.workbook) return false
        if (user != other.user) return false

        return true
    }

    override fun hashCode(): Int {
        var result = workbook?.hashCode() ?: 0
        result = 31 * result + (user?.hashCode() ?: 0)
        return result
    }
}