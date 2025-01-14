package com.solve.domain.problem.domain.entity

import com.solve.domain.user.domain.entity.User
import com.solve.global.common.entity.BaseTimeEntity
import com.solve.global.common.enums.ProgrammingLanguage
import jakarta.persistence.*

@Entity
@Table(name = "problem_code")
class ProblemCode(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    val problem: Problem,

    var code: String,

    var language: ProgrammingLanguage
) : BaseTimeEntity()