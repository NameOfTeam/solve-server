package com.solve.domain.run.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import com.solve.global.common.enums.ProgrammingLanguage
import jakarta.persistence.*

@Entity
@Table(name = "runs")
class Run(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    val author: User,

    @Column(nullable = false)
    val code: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    val language: ProgrammingLanguage
) : BaseTimeEntity()