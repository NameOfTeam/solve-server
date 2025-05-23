package com.solve.domain.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class ReissueRequest(
    @field:NotBlank
    val refreshToken: String
)
