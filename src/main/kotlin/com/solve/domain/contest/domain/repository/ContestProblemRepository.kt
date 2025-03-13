package com.solve.domain.contest.domain.repository

import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.entity.ContestProblem
import com.solve.domain.problem.domain.entity.Problem
import org.springframework.data.jpa.repository.JpaRepository

interface ContestProblemRepository : JpaRepository<ContestProblem, Long> {
    fun findByContestAndProblem(contest: Contest, problem: Problem): ContestProblem?
    fun findAllByContest(contest: Contest): List<ContestProblem>

    fun existsByContestAndProblem(contest: Contest, problem: Problem): Boolean
}