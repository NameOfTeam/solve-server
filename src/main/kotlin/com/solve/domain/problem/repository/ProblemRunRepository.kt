package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.ProblemRun
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemRunRepository : JpaRepository<ProblemRun, Long> {
}