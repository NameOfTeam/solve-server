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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "problem_test_case_inputs", joinColumns = [JoinColumn(name = "test_case_id")])
    @OrderColumn
    @Column(name = "input", nullable = false, columnDefinition = "TEXT")
    var input: List<String>,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "problem_test_case_outputs", joinColumns = [JoinColumn(name = "test_case_id")])
    @OrderColumn
    @Column(name = "output", nullable = false, columnDefinition = "TEXT")
    var output: List<String>,

    @Column(name = "sample", nullable = false)
    var sample: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val problem: Problem,
)