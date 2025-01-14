package com.solve.domain.problem.domain.entity

import com.solve.global.common.entity.BaseTimeEntity
import jakarta.persistence.*
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(name = "problem_examples")
class ProblemExample(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "input", nullable = false)
    var input: String,

    @Column(name = "output", nullable = false)
    var output: String,

    @Column(name = "description")
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    val problem: Problem
): BaseTimeEntity()