package com.solve.domain.workbook.repository

import com.solve.domain.workbook.domain.entity.Workbook
import com.solve.domain.workbook.domain.enums.WorkbookSearchFilter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface WorkbookQueryRepository {
    fun searchWorkbook(query: String, filter: WorkbookSearchFilter?, pageable: Pageable): Page<Workbook>
}