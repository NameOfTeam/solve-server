package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.problem.domain.entity.ProblemSubmit
import com.solve.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemSubmitRepository : JpaRepository<ProblemSubmit, Long> {
    fun findAllByProblem(problem: Problem): List<ProblemSubmit>
    fun findAllByAuthor(author: User): List<ProblemSubmit>
    fun findAllByProblemAndAuthor(problem: Problem, author: User): List<ProblemSubmit>
}