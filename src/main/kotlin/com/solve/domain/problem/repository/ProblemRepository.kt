package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.Problem
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemRepository : JpaRepository<Problem, Long>