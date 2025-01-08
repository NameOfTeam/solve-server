package com.solve.domain.admin.workbook.dto.request

data class AdminCreateWorkbookRequest(
    val title: String,
    val description: String?,
    val problemIds: List<Long>
)