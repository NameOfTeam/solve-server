package com.solve.domain.contest.dto.response

import com.solve.domain.contest.domain.entity.ContestAnnouncement
import com.solve.domain.user.domain.entity.User
import java.time.LocalDateTime
import java.util.UUID

data class ContestAnnouncementResponse(
    val id: Long,
    val title: String,
    val content: String,
    val author: ContestAnnouncementAuthorResponse,
    val important: Boolean,
    val publishedAt: LocalDateTime,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun of(announcement: ContestAnnouncement) = ContestAnnouncementResponse(
            id = announcement.id!!,
            title = announcement.title,
            content = announcement.content,
            author = ContestAnnouncementAuthorResponse.of(announcement.author),
            important = announcement.important,
            publishedAt = announcement.publishedAt,
            createdAt = announcement.createdAt,
            updatedAt = announcement.updatedAt
        )
    }
}

data class ContestAnnouncementAuthorResponse(
    val id: UUID,
    val username: String,
) {
    companion object {
        fun of(author: User) = ContestAnnouncementAuthorResponse(
            id = author.id!!,
            username = author.username
        )
    }
}