package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemCode
import com.solve.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemCodeRepository : JpaRepository<ProblemCode, Long> {
    fun findByProblemAndUser(problem: Problem, user: User): ProblemCode?
    fun existsByProblemAndUser(problem: Problem, user: User): Boolean
}