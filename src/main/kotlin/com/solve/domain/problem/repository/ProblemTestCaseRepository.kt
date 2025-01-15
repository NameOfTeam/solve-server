package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemTestCase
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemTestCaseRepository: JpaRepository<ProblemTestCase, Long> {
    fun findAllByProblem(problem: Problem): List<ProblemTestCase>
    fun findByProblemAndId(problem: Problem, id: Long): ProblemTestCase?
}