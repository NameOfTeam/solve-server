package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.ProblemCode
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemCodeRepository : JpaRepository<ProblemCode, Long>