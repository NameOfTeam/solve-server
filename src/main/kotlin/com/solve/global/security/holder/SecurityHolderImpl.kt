package com.solve.global.security.holder

import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityHolderImpl(
    private val userRepository: UserRepository
) : SecurityHolder {
    override val email: String
        get() = SecurityContextHolder.getContext().authentication.name

    override val user: User
        get() = userRepository.findByEmail(email) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_EMAIL, email)

    override val isAuthenticated: Boolean
        get() {
            val authentication = SecurityContextHolder.getContext().authentication
            return authentication != null &&
                    authentication.isAuthenticated &&
                    authentication.name != "anonymousUser"
        }
}