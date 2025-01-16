package com.solve.domain.run.repository

import com.solve.domain.run.domain.entity.Run
import org.springframework.data.jpa.repository.JpaRepository

interface RunRepository : JpaRepository<Run, Long>