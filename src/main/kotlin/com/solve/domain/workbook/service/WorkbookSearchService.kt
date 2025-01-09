package com.solve.domain.workbook.service

import com.solve.domain.workbook.domain.enums.WorkbookSearchFilter
import com.solve.domain.workbook.dto.response.WorkbookResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface WorkbookSearchService {
    fun searchWorkbook(query: String, filter: WorkbookSearchFilter?, pageable: Pageable): Page<WorkbookResponse>
}