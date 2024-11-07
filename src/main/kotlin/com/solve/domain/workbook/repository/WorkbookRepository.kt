package com.solve.domain.workbook.repository

import com.solve.domain.workbook.domain.entity.Workbook
import org.springframework.data.jpa.repository.JpaRepository

interface WorkbookRepository : JpaRepository<Workbook, Long>