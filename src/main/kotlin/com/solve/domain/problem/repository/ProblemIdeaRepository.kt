package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemIdea
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemIdeaRepository : JpaRepository<ProblemIdea, Long> {
    fun findAllByProblem(problem: Problem): List<ProblemIdea>
    fun findByIdAndProblem(id: Long, problem: Problem): ProblemIdea?
}