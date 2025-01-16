package com.solve.domain.run.dto.request

data class RunWebSocketMessage(
    val type: String = "",
    val input: String? = null
)