package com.solve.domain.contest.domain.repository

import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.entity.ContestParticipant
import com.solve.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ContestParticipantRepository : JpaRepository<ContestParticipant, Long> {
    fun findByContestAndUser(contest: Contest, user: User): ContestParticipant?
    fun findAllByContest(contest: Contest): List<ContestParticipant>

    fun existsByContestAndUser(contest: Contest, user: User): Boolean

    fun countByUser(user: User): Long
}