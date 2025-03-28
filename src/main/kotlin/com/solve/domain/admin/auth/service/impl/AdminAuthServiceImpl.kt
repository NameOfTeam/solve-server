package com.solve.domain.admin.auth.service.impl

import com.solve.domain.admin.auth.dto.request.AdminSignUpRequest
import com.solve.domain.admin.auth.error.AdminAuthError
import com.solve.domain.admin.auth.service.AdminAuthService
import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.domain.enums.UserRole
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.config.admin.AdminProperties
import com.solve.global.config.file.FileProperties
import com.solve.global.error.CustomException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.nio.channels.Channels
import java.nio.file.Paths

@Service
class AdminAuthServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val adminProperties: AdminProperties,
    private val userRepository: UserRepository,
    private val fileProperties: FileProperties,
) : AdminAuthService {
    @Transactional
    override fun signup(request: AdminSignUpRequest) {
        if (userRepository.existsByEmail(request.email)) throw CustomException(UserError.EMAIL_DUPLICATED)
        if (userRepository.existsByUsername(request.username)) throw CustomException(UserError.USERNAME_DUPLICATED)
        if (request.key != adminProperties.key) throw CustomException(AdminAuthError.INVALID_KEY)

        val user = userRepository.save(
            User(
                username = request.username,
                email = request.email,
                password = passwordEncoder.encode(request.password),
                role = UserRole.ADMIN,
                isVerified = true
            )
        )

        val directory = Paths.get(fileProperties.path, "avatars").toFile()
        if (!directory.exists()) directory.mkdirs()

        URI("https://drive.google.com/uc?export=download&id=16rg6-0Bf2ih-qpF0WUtcmzUmt4mty2Fe").toURL().openStream()
            .use { input ->
                Channels.newChannel(input).use { rbc ->
                    FileOutputStream(File(directory, "${user.id}.webp")).use { output ->
                        output.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
                    }
                }
            }
    }
}