package com.solve.global.security.holder

import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import org.springframework.messaging.simp.SimpAttributesContextHolder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SecurityHolderImpl(
    private val userRepository: UserRepository
) : SecurityHolder {
    override val email: String
        get() = if (isWebSocket) SimpAttributesContextHolder.currentAttributes()
            .getAttribute("email") as String else SecurityContextHolder.getContext().authentication.name

    override val user: User
        get() = userRepository.findByEmail(email) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_EMAIL, email)

    override val isAuthenticated: Boolean
        get() = email != "anonymousUser"

    private val isWebSocket: Boolean
        get() {
            try {
                SimpAttributesContextHolder.currentAttributes()

                return true
            } catch (e: IllegalStateException) {
                return false
            }
        }
}