package com.solve.domain.user.service

import com.solve.domain.user.dto.request.UserMeAddConnectionRequest
import com.solve.domain.user.dto.response.UserMeResponse
import java.util.*

interface UserMeConnectionService {
    fun addConnection(request: UserMeAddConnectionRequest): UserMeResponse
    fun removeConnection(connectionId: UUID): UserMeResponse
}