package com.solve.domain.contest.domain.entity

import com.solve.domain.contest.domain.enums.ContestVisibility
import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner_id")
    var winner: User? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false)
    var visibility: ContestVisibility = ContestVisibility.PUBLIC,

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val operators: MutableSet<ContestOperator> = mutableSetOf(),

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val participants: MutableSet<ContestParticipant> = mutableSetOf(),

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val problems: MutableSet<ContestProblem> = mutableSetOf(),
) : BaseTimeEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Contest) return false
        if (id == null || other.id == null) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    fun addOperator(operator: ContestOperator) {
        operators.add(operator)
    }

    fun addParticipant(participant: ContestParticipant) {
        participants.add(participant)
    }

    fun addProblem(problem: ContestProblem) {
        problems.add(problem)
    }
}