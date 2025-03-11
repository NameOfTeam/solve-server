package com.solve.domain.contest.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "contest_announcements")
class ContestAnnouncement(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    val contest: Contest,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BINARY(16)")
    val author: User,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "content", nullable = false)
    var content: String,

    @Column(name = "important", nullable = false)
    var important: Boolean,

    @Column(name = "published_at", nullable = false)
    var publishedAt: LocalDateTime = LocalDateTime.now()
): BaseTimeEntity();