package com.solve.domain.problemset.domain.entity

import com.solve.domain.problem.domain.entity.Problem
import jakarta.persistence.*

@Entity
@Table(name = "problem_set_problems")
class ProblemSetProblem(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_set_id", nullable = false)
    val problemSet: ProblemSet,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,
)