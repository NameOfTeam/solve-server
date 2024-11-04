package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemIdea
import com.solve.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemIdeaRepository : JpaRepository<ProblemIdea, Long> {
    fun findAllByProblemAndAuthor(problem: Problem, author: User): List<ProblemIdea>
    fun findByIdAndProblem(id: Long, problem: Problem): ProblemIdea?
}