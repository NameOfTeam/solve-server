package com.solve.domain.user.service.impl

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.webp.WebpWriter
import com.solve.domain.user.dto.request.UserMePasswordUpdateRequest
import com.solve.domain.user.dto.request.UserMeUpdateRequest
import com.solve.domain.user.dto.response.UserMeResponse
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.domain.user.service.UserMeService
import com.solve.global.config.file.FileProperties
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class UserMeServiceImpl(
    private val securityHolder: SecurityHolder,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val userRepository: UserRepository,
    private val fileProperties: FileProperties
) : UserMeService {
    @Transactional(readOnly = true)
    override fun getMe(): UserMeResponse {
        val user = securityHolder.user

        return UserMeResponse.of(user)
    }

    @Transactional
    override fun updateMe(request: UserMeUpdateRequest): UserMeResponse {
        var user = securityHolder.user

        if (request.username != null) user.username = request.username

        user = userRepository.save(user)

        return UserMeResponse.of(user)
    }

    @Transactional
    override fun updatePassword(request: UserMePasswordUpdateRequest): UserMeResponse {
        var user = securityHolder.user

        if (!passwordEncoder.matches(
                request.currentPassword,
                user.password
            )
        ) throw CustomException(UserError.INVALID_PASSWORD)

        user.password = passwordEncoder.encode(request.newPassword)

        user = userRepository.save(user)

        return UserMeResponse.of(user)
    }

    @Transactional
    override fun updateAvatar(file: MultipartFile): UserMeResponse {
        if (file.isEmpty) throw CustomException(UserError.EMPTY_FILE)

        val user = securityHolder.user
        val directory = File(fileProperties.path, "avatars")

        if (!directory.exists()) directory.mkdirs()

        val png = File(directory, "${user.id}.png")

        file.transferTo(png)

        ImmutableImage.loader()
            .fromFile(png)
            .scaleTo(256, 256)
            .output(WebpWriter.DEFAULT.withLossless(), File(directory, "${user.id}.webp"))

        png.delete()

        return UserMeResponse.of(user)
    }
}