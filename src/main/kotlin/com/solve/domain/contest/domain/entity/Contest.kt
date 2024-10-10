package com.solve.domain.contest.domain.entity

import com.solve.domain.problem.domain.entity.Problem
import com.solve.global.common.BaseTimeEntity
import jakarta.persistence.*

@Entity
@Table(name = "contests")
class Contest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String,

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val operators: MutableList<ContestOperator> = mutableListOf(),

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val participant: MutableList<ContestParticipant> = mutableListOf(),

    @OneToMany(mappedBy = "contest", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    val problems: MutableList<ContestProblem> = mutableListOf(),
): BaseTimeEntity()