package com.solve.domain.workbook.domain.entity

import com.solve.domain.user.domain.entity.User
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(
    name = "workbook_bookmarks",
    indexes = [
        Index(name = "idx_workbook_bookmark_user", columnList = "user_id"),
        Index(name = "idx_workbook_bookmark_workbook", columnList = "workbook_id")
    ]
)
@IdClass(WorkbookBookmarkId::class)
class WorkbookBookmark(
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
        if (other !is WorkbookBookmark) return false

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

class WorkbookBookmarkId(
    val workbook: Long? = null,
    val user: Long? = null
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is WorkbookBookmarkId) return false

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