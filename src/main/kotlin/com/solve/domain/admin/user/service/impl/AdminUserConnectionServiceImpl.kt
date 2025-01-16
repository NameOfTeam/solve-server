package com.solve.domain.admin.user.service.impl

import com.solve.domain.admin.user.dto.request.AdminUserConnectionAddRequest
import com.solve.domain.admin.user.error.AdminUserConnectionError
import com.solve.domain.admin.user.service.AdminUserConnectionService
import com.solve.domain.user.domain.entity.UserConnection
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserConnectionRepository
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AdminUserConnectionServiceImpl(
    private val userRepository: UserRepository,
    private val userConnectionRepository: UserConnectionRepository
) : AdminUserConnectionService {
    @Transactional
    override fun addUserConnection(userId: UUID, request: AdminUserConnectionAddRequest) {
        val user = userRepository.findByIdOrNull(userId) ?: throw CustomException(
            UserError.USER_NOT_FOUND_BY_ID,
            userId.toString()
        )

        userConnectionRepository.save(
            userConnectionRepository.findByUserAndType(user, request.type)?.apply {
                value = request.value
            } ?: UserConnection(
                user = user,
                type = request.type,
                value = request.value
            )
        )
    }

    @Transactional
    override fun removeUserConnection(userId: UUID, connectionId: UUID) {
        val user = userRepository.findByIdOrNull(userId) ?: throw CustomException(
            UserError.USER_NOT_FOUND_BY_ID,
            userId.toString()
        )
        val connection = userConnectionRepository.findByUserAndId(user, connectionId) ?: throw CustomException(
            AdminUserConnectionError.USER_CONNECTION_NOT_FOUND,
            connectionId.toString()
        )

        userConnectionRepository.delete(connection)
    }
}