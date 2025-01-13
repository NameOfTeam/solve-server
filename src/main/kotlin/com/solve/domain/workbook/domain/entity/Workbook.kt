package com.solve.domain.workbook.domain.entity

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.user.domain.entity.User
import com.solve.domain.workbook.error.WorkbookBookmarkError
import com.solve.domain.workbook.error.WorkbookLikeError
import com.solve.domain.workbook.error.WorkbookProblemError
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String?,

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "workbook", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val problems: MutableSet<WorkbookProblem> = mutableSetOf(),

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "workbook", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val likes: MutableSet<WorkbookLike> = mutableSetOf(),

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

    fun addLike(user: User) {
        if (hasLikedBy(user)) {
            throw CustomException(WorkbookLikeError.WORKBOOK_LIKE_ALREADY_EXISTS)
        }
        likes.add(WorkbookLike(this, user))
    }

    fun removeLike(user: User) {
        val like = likes.find { it.user == user }
            ?: throw CustomException(WorkbookLikeError.WORKBOOK_LIKE_NOT_FOUND)
        likes.remove(like)
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

    fun addProblem(problem: Problem) {
        if (hasProblem(problem)) {
            throw CustomException(WorkbookProblemError.WORKBOOK_PROBLEM_ALREADY_EXISTS)
        }

        problems.add(WorkbookProblem(this, problem))
    }

    private fun hasLikedBy(user: User): Boolean {
        return likes.any { it.user == user }
    }

    private fun hasBookmarkedBy(user: User): Boolean {
        return bookmarks.any { it.user == user }
    }

    private fun hasProblem(problem: Problem): Boolean {
        return problems.any { it.problem == problem }
    }
}