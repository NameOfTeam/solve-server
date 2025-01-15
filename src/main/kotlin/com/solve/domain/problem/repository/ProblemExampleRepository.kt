package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemExample
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemExampleRepository: JpaRepository<ProblemExample, Long> {
    fun findAllByProblem(problem: Problem): List<ProblemExample>
    fun findByProblemAndId(problem: Problem, id: Long): ProblemExample?
}