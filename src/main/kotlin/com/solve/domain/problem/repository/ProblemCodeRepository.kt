package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemCode
import com.solve.domain.user.domain.entity.User
import com.solve.global.common.enums.ProgrammingLanguage
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemCodeRepository : JpaRepository<ProblemCode, Long> {
    fun findByProblemAndUserAndLanguage(problem: Problem, user: User, language: ProgrammingLanguage): ProblemCode?
}