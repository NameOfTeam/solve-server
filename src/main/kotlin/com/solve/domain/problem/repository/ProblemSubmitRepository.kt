package com.solve.domain.problem.repository

import com.solve.domain.problem.domain.entity.ProblemSubmit
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemSubmitRepository : JpaRepository<ProblemSubmit, Long>