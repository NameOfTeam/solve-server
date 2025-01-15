package com.solve.domain.contest.repository

import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.entity.ContestOperator
import com.solve.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ContestOperatorRepository: JpaRepository<ContestOperator, Long> {
    fun findAllByContest(contest: Contest): List<ContestOperator>
    fun findByContestAndUser(contest: Contest, user: User): ContestOperator?
    fun existsByContestAndUser(contest: Contest, user: User): Boolean
}