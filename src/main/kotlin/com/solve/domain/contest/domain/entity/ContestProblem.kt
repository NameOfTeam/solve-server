package com.solve.domain.contest.domain.entity

import com.solve.domain.problem.domain.entity.Problem
import jakarta.persistence.*

@Entity
@Table(name = "contest_problems")
class ContestProblem(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contest_id", nullable = false)
    val contest: Contest,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,
)