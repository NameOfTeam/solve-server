package com.solve.domain.auth.service.impl

import com.solve.domain.auth.dto.request.LoginRequest
import com.solve.domain.auth.dto.request.ReissueRequest
import com.solve.domain.auth.dto.request.SignUpRequest
import com.solve.domain.auth.dto.response.VerifyResponse
import com.solve.domain.auth.error.AuthError
import com.solve.domain.auth.repository.RefreshTokenRepository
import com.solve.domain.auth.repository.VerificationTokenRepository
import com.solve.domain.auth.service.AuthService
import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import com.solve.global.security.jwt.dto.JwtResponse
import com.solve.global.security.jwt.enums.JwtType
import com.solve.global.security.jwt.error.JwtError
import com.solve.global.security.jwt.provider.JwtProvider
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.FileCopyUtils
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*

@Service
class AuthServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val mailProperties: MailProperties,
    private val mailSender: JavaMailSender,
    private val verificationTokenRepository: VerificationTokenRepository
) : AuthService {
    @Transactional
    override fun login(request: LoginRequest): JwtResponse {
        val user = userRepository.findByEmail(request.email) ?: throw CustomException(
            UserError.USER_NOT_FOUND_BY_EMAIL,
            request.email
        )

        if (!user.verified) throw CustomException(UserError.USER_NOT_VERIFIED)

        if (!passwordEncoder.matches(
                request.password,
                user.password
            )
        ) throw CustomException(UserError.PASSWORD_NOT_MATCH)

        return jwtProvider.generateToken(user.email)
    }

    @Transactional
    override fun signup(request: SignUpRequest) {
        if (userRepository.existsByUsername(request.username)) throw CustomException(UserError.USERNAME_DUPLICATED)
        if (userRepository.existsByEmail(request.email)) throw CustomException(UserError.EMAIL_DUPLICATED)

        var user = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            verified = false
        )
        user = userRepository.save(user)

        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        helper.setFrom(mailProperties.username)
        helper.setTo(user.email)
        helper.setSubject("Verify Solve Login")

        val verificationToken = generateVerificationToken()
        val verificationLink = "http://localhost:8080/auth/verify?token=$verificationToken"

        val resource = ClassPathResource("/verify.html")
        val content = FileCopyUtils.copyToString(resource.inputStream.reader(StandardCharsets.UTF_8))
            .replace("{verificationLink}", verificationLink)

        helper.setText(content, true)

        mailSender.send(message)

        verificationTokenRepository.setVerificationToken(verificationToken, user.email)
    }

    @Transactional
    override fun reissue(request: ReissueRequest): JwtResponse {
        if (jwtProvider.getType(request.refreshToken) != JwtType.REFRESH) throw CustomException(JwtError.INVALID_TOKEN_TYPE)

        val email = jwtProvider.getEmail(request.refreshToken)
        val user = userRepository.findByEmail(email) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_EMAIL, email)
        val refreshToken =
            refreshTokenRepository.getRefreshToken(user.email) ?: throw CustomException(JwtError.INVALID_TOKEN)

        if (refreshToken != request.refreshToken) throw CustomException(JwtError.INVALID_TOKEN)

        return jwtProvider.generateToken(user.email)
    }

    @Transactional
    override fun verify(token: String): VerifyResponse {
        if (!verificationTokenRepository.existsVerificationToken(token)) throw CustomException(AuthError.INVALID_VERIFICATION_TOKEN)

        val email = verificationTokenRepository.getVerificationToken(token)
            ?: throw CustomException(AuthError.INVALID_VERIFICATION_TOKEN)
        val user = userRepository.findByEmail(email) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_EMAIL)

        user.verified = true

        userRepository.save(user)

        return VerifyResponse(true)
    }

    private val secureRandom = SecureRandom()
    private val base64Encoder = Base64.getUrlEncoder().withoutPadding()

    private fun generateVerificationToken() =
        base64Encoder.encodeToString(ByteArray(32).also { secureRandom.nextBytes(it) })
}