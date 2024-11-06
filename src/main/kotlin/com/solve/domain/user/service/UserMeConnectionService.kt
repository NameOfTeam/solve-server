package com.solve.domain.user.service

import com.solve.domain.user.dto.request.UserMeAddConnectionRequest
import java.util.*

interface UserMeConnectionService {
    fun addConnection(request: UserMeAddConnectionRequest)
    fun removeConnection(connectionId: UUID)
}