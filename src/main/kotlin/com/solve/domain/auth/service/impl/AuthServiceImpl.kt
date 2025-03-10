package com.solve.domain.auth.service.impl

import com.solve.domain.auth.domain.entity.EmailVerification
import com.solve.domain.auth.dto.request.LoginRequest
import com.solve.domain.auth.dto.request.ReissueRequest
import com.solve.domain.auth.dto.request.SignUpRequest
import com.solve.domain.auth.dto.response.VerifyResponse
import com.solve.domain.auth.error.AuthError
import com.solve.domain.auth.repository.EmailVerificationRepository
import com.solve.domain.auth.repository.RefreshTokenRepository
import com.solve.domain.auth.service.AuthService
import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.config.file.FileProperties
import com.solve.global.config.frontend.FrontendProperties
import com.solve.global.error.CustomException
import com.solve.global.security.jwt.dto.JwtResponse
import com.solve.global.security.jwt.enums.JwtType
import com.solve.global.security.jwt.error.JwtError
import com.solve.global.security.jwt.provider.JwtProvider
import org.springframework.boot.autoconfigure.mail.MailProperties
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.messaging.MessagingException
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.FileCopyUtils
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.nio.channels.Channels
import java.nio.charset.StandardCharsets
import java.nio.file.Paths
import java.security.SecureRandom
import java.time.LocalDateTime
import java.util.*

@Service
class AuthServiceImpl(
    private val frontendProperties: FrontendProperties,
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val mailProperties: MailProperties,
    private val mailSender: JavaMailSender,
    private val emailVerificationRepository: EmailVerificationRepository,
    private val fileProperties: FileProperties,
) : AuthService {
    @Transactional
    override fun login(request: LoginRequest): JwtResponse {
        val user = userRepository.findByEmail(request.email) ?: throw CustomException(
            UserError.USER_NOT_FOUND_BY_EMAIL,
            request.email
        )

        if (!user.isVerified) throw CustomException(UserError.USER_NOT_VERIFIED)

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
        if (request.password != request.passwordConfirm) throw CustomException(AuthError.PASSWORD_MISMATCH)

        val verificationToken = generateVerificationToken()
        val verificationLink = "${frontendProperties.url}/verify?token=$verificationToken"

        emailVerificationRepository.save(
            EmailVerification(
                verificationToken = verificationToken,
                email = request.email,
                expiredAt = LocalDateTime.now().plusMinutes(5)
            )
        )

        val resource = ClassPathResource("/verify.html")
        val content = FileCopyUtils.copyToString(resource.inputStream.reader(StandardCharsets.UTF_8))
            .replace("{verificationLink}", verificationLink)

        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")
        helper.setFrom(mailProperties.username)
        helper.setTo(request.email)
        helper.setSubject("Verify Solve Login")
        helper.setText(content, true)

        try {
            mailSender.send(message)
        } catch (e: MessagingException) {
            throw CustomException(AuthError.EMAIL_NOT_FOUND)
        }

        val user = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
        )
        userRepository.save(user)
    }

    @Transactional
    override fun reissue(request: ReissueRequest): JwtResponse {
        if (jwtProvider.getType(request.refreshToken) != JwtType.REFRESH) throw CustomException(JwtError.INVALID_TOKEN_TYPE)

        val email = jwtProvider.getEmail(request.refreshToken)
        val user = userRepository.findByEmail(email) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_EMAIL, email)
        val refreshToken =
            refreshTokenRepository.getRefreshToken(user.email) ?: throw CustomException(JwtError.INVALID_TOKEN)

        if (refreshToken != request.refreshToken) throw CustomException(JwtError.INCORRECT_TOKEN)

        return jwtProvider.generateToken(user.email)
    }

    @Transactional
    override fun verify(token: String): VerifyResponse {
        if (!emailVerificationRepository.existsByVerificationToken(token)) throw CustomException(AuthError.INVALID_VERIFICATION_TOKEN)

        val emailVerification = emailVerificationRepository.findByVerificationToken(token)
            ?: throw CustomException(AuthError.INVALID_VERIFICATION_TOKEN)

        if (emailVerification.isVerified) {
            throw CustomException(AuthError.ALREADY_VERIFIED)
        }

        if (emailVerification.expiredAt.isBefore(LocalDateTime.now())) {
            throw CustomException(AuthError.VERIFICATION_EXPIRED)
        }

        val user = userRepository.findByEmail(emailVerification.email)
            ?: throw CustomException(UserError.USER_NOT_FOUND_BY_EMAIL)

        emailVerification.isVerified = true
        emailVerificationRepository.save(emailVerification)

        user.isVerified = true
        userRepository.save(user)

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

        return VerifyResponse(true)
    }

    @Scheduled(fixedRate = 100)
    @Transactional
    fun deleteExpiredVerificationTokens() {
        val expiredVerifications =
            emailVerificationRepository.findAllByExpiredAtBeforeAndIsVerifiedFalse(LocalDateTime.now())

        for (verification in expiredVerifications) {
            val user = userRepository.findByEmail(verification.email) ?: continue

            if (!user.isVerified) {
                userRepository.delete(user)
            }

            emailVerificationRepository.delete(verification)
        }
    }

    private val secureRandom = SecureRandom()
    private val base64Encoder = Base64.getUrlEncoder().withoutPadding()

    private fun generateVerificationToken() =
        base64Encoder.encodeToString(ByteArray(32).also { secureRandom.nextBytes(it) })
}