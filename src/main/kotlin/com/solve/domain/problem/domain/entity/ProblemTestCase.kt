package com.solve.domain.problem.domain.entity

import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "problem_test_cases")
class ProblemTestCase(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "input", nullable = false, columnDefinition = "TEXT")
    var input: String,

    @Column(name = "output", nullable = false, columnDefinition = "TEXT")
    var output: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val problem: Problem,
)