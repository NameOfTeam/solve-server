package com.solve.domain.workbook.dto.request

data class CreateWorkbookRequest(
    val title: String,
    val description: String?,
    val problemIds: List<Long>
)