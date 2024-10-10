package com.devox.domain.auth.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AuthCheckResponse(
    val email: Boolean?,
    val linkupId: Boolean?,
    val phoneNumber: Boolean?
)
