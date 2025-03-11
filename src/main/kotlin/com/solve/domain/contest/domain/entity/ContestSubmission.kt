package com.solve.domain.contest.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "contest_submissions")
class ContestSubmission(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    val participant: ContestParticipant,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: ContestProblem,

    val code: String,
    val language: String,
    val result: String? = null,
    val score: Int? = null
)