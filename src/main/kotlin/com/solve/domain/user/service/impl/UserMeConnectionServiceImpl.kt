package com.solve.domain.user.service.impl

import com.solve.domain.user.domain.entity.UserConnection
import com.solve.domain.user.dto.request.UserMeAddConnectionRequest
import com.solve.domain.user.error.UserConnectionError
import com.solve.domain.user.repository.UserConnectionRepository
import com.solve.domain.user.repository.UserRepository
import com.solve.domain.user.service.UserMeConnectionService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserMeConnectionServiceImpl(
    private val securityHolder: SecurityHolder,
    private val userRepository: UserRepository,
    private val userConnectionRepository: UserConnectionRepository
) : UserMeConnectionService {
    @Transactional
    override fun addConnection(request: UserMeAddConnectionRequest) {
        val user = securityHolder.user
        val connection = userConnectionRepository.findByUserAndType(user, request.type)

        if (connection != null) {
            connection.value = request.value
        } else {
            userConnectionRepository.save(
                UserConnection(
                    user = user,
                    type = request.type,
                    value = request.value
                )
            )
        }

        userRepository.save(user)
    }

    @Transactional
    override fun removeConnection(connectionId: UUID) {
        val user = securityHolder.user
        val connection = userConnectionRepository.findByUserAndId(user, connectionId) ?: throw CustomException(
            UserConnectionError.CONNECTION_NOT_FOUND
        )

        userConnectionRepository.delete(connection)
    }
}