package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemContributor
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemContributorRepository : JpaRepository<ProblemContributor, Long> {
    fun findAllByProblem(problem: Problem): List<ProblemContributor>

}