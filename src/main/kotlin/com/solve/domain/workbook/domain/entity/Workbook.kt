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

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "workbook", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val bookmarks: MutableSet<WorkbookBookmark> = mutableSetOf(),

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

    fun addBookmark(user: User) {
        if (hasBookmarkedBy(user)) {
            throw CustomException(WorkbookBookmarkError.WORKBOOK_BOOKMARK_ALREADY_EXISTS)
        }
        bookmarks.add(WorkbookBookmark(this, user))
    }

    fun removeBookmark(user: User) {
        val bookmark = bookmarks.find { it.user == user }
            ?: throw CustomException(WorkbookBookmarkError.WORKBOOK_BOOKMARK_NOT_FOUND)
        bookmarks.remove(bookmark)
    }

    private fun hasBookmarkedBy(user: User): Boolean {
        return bookmarks.any { it.user == user }
    }
}