package com.solve.domain.submit.repository

import com.solve.domain.problem.domain.entity.Problem
import com.solve.domain.submit.domain.entity.Submit
import com.solve.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface SubmitRepository : JpaRepository<Submit, Long> {
    fun findAllByProblem(problem: Problem): List<Submit>
    fun findAllByProblemAndAuthor(problem: Problem, author: User): List<Submit>

    fun countByAuthor(author: User): Long
}