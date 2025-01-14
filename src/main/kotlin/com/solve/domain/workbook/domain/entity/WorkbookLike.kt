package com.solve.domain.workbook.domain.entity

import com.solve.domain.user.domain.entity.User
import jakarta.persistence.*

@Entity
@Table(name = "workbook_likes")
class WorkbookLike(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workbook_id", nullable = false)
    val workbook: Workbook,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
)