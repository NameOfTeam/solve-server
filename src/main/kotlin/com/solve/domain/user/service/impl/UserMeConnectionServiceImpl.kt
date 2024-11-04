package com.solve.domain.user.service.impl

import com.solve.domain.user.domain.entity.UserConnection
import com.solve.domain.user.dto.request.UserMeAddConnectionRequest
import com.solve.domain.user.dto.response.UserMeResponse
import com.solve.domain.user.repository.UserRepository
import com.solve.domain.user.service.UserMeConnectionService
import com.solve.global.security.holder.SecurityHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserMeConnectionServiceImpl(
    private val securityHolder: SecurityHolder,
    private val userRepository: UserRepository
) : UserMeConnectionService {
    @Transactional
    override fun addConnection(request: UserMeAddConnectionRequest): UserMeResponse {
        val user = securityHolder.user

        user.connections.add(
            UserConnection(
                user = user,
                type = request.type,
                value = request.value
            )
        )

        userRepository.save(user)

        return UserMeResponse.of(user)
    }

    @Transactional
    override fun removeConnection(connectionId: UUID): UserMeResponse {
        val user = securityHolder.user

        user.connections.removeIf { it.id == connectionId }

        userRepository.save(user)

        return UserMeResponse.of(user)
    }
}