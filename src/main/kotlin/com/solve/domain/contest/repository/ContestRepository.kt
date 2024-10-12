package com.solve.domain.contest.repository

import com.solve.domain.contest.domain.entity.Contest
import org.springframework.data.jpa.repository.JpaRepository

interface ContestRepository : JpaRepository<Contest, Long>