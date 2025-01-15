package com.solve.domain.workbook.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.domain.workbook.error.WorkbookBookmarkError
import com.solve.domain.workbook.error.WorkbookLikeError
import com.solve.global.common.entity.BaseTimeEntity
import com.solve.global.error.CustomException
import jakarta.persistence.*
import org.hibernate.annotations.BatchSize

@Entity
@Table(
    name = "workbooks",
    indexes = [
        Index(name = "idx_workbook_author", columnList = "author_id")
    ]
)
class Workbook(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User
) : BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Workbook) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}