package com.solve.domain.user.dto.response

import com.solve.domain.user.domain.entity.UserConnection
import java.util.*

data class UserMeConnectionResponse(
    val id: UUID,
    val type: String,
    val value: String
) {
    companion object {
        fun of(connection: UserConnection) = UserMeConnectionResponse(
            id = connection.id!!,
            type = connection.type.name,
            value = connection.value
        )
    }
}