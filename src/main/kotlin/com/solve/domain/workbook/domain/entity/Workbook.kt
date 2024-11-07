package com.solve.domain.workbook.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.domain.workbook.error.WorkbookLikeError
import com.solve.global.common.BaseTimeEntity
import com.solve.global.error.CustomException
import jakarta.persistence.*

@Entity
@Table(name = "workbooks")
class Workbook(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @OneToMany(mappedBy = "workbook", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val problems: MutableList<WorkbookProblem> = mutableListOf(),

    @OneToMany(mappedBy = "workbook", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: MutableList<WorkbookLike> = mutableListOf(),

    @OneToMany(mappedBy = "workbook", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val bookmarks: MutableList<WorkbookBookmark> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User
): BaseTimeEntity() {
    fun addLike(user: User) {
        likes.add(WorkbookLike(this, user))
    }

    fun removeLike(user: User) {
        val like = likes.find { it.user == user } ?: throw CustomException(WorkbookLikeError.WORKBOOK_LIKE_NOT_FOUND)

        likes.remove(like)
    }

    fun addBookmark(user: User) {
        bookmarks.add(WorkbookBookmark(this, user))
    }

    fun removeBookmark(user: User) {
        val bookmark = bookmarks.find { it.user == user } ?: throw CustomException(WorkbookLikeError.WORKBOOK_LIKE_NOT_FOUND)

        bookmarks.remove(bookmark)
    }
}