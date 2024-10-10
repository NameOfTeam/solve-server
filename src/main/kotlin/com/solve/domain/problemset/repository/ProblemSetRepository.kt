package com.solve.domain.problemset.repository

import com.solve.domain.problemset.domain.entity.ProblemSet
import org.springframework.data.jpa.repository.JpaRepository

interface ProblemSetRepository: JpaRepository<ProblemSet, Long>