package com.solve.domain.contest.domain.repository

import com.solve.domain.contest.domain.entity.Contest
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ContestRepository : JpaRepository<Contest, Long> {
    fun countByEndTimeGreaterThanEqualAndStartTimeLessThanEqual(endAt: LocalDateTime, startAt: LocalDateTime): Long
}