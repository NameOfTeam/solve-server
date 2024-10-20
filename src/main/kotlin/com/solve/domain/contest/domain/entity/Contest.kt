package com.solve.domain.contest.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.global.common.BaseTimeEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "contests")
class Contest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @Column(name = "description", nullable = false)
    var description: String,

    @Column(name = "start_at", nullable = false)
    var startAt: LocalDateTime,

    @Column(name = "end_at", nullable = false)
    var endAt: LocalDateTime,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    var owner: User,

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val operators: MutableList<ContestOperator> = mutableListOf(),

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val participants: MutableList<ContestParticipant> = mutableListOf(),

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val problems: MutableList<ContestProblem> = mutableListOf(),
) : BaseTimeEntity()