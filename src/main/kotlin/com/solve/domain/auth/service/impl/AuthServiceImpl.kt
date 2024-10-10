package com.solve.domain.auth.service.impl

import com.solve.domain.auth.dto.request.LoginRequest
import com.solve.domain.auth.dto.request.ReissueRequest
import com.solve.domain.auth.dto.request.SignUpRequest
import com.solve.domain.auth.repository.RefreshTokenRepository
import com.solve.domain.auth.service.AuthService
import com.solve.domain.user.domain.entity.User
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import com.solve.global.security.jwt.dto.JwtResponse
import com.solve.global.security.jwt.enums.JwtType
import com.solve.global.security.jwt.error.JwtError
import com.solve.global.security.jwt.provider.JwtProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthServiceImpl(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
    private val refreshTokenRepository: RefreshTokenRepository
) : AuthService {
    @Transactional
    override fun login(request: LoginRequest): JwtResponse {
        val user = userRepository.findByEmail(request.email) ?: throw CustomException(
            UserError.USER_NOT_FOUND_BY_EMAIL,
            request.email
        )

        if (!passwordEncoder.matches(
                request.password,
                user.password
            )
        ) throw CustomException(UserError.PASSWORD_NOT_MATCH)

        return jwtProvider.generateToken(user.email)
    }

    @Transactional
    override fun signup(request: SignUpRequest) {
        if (userRepository.existsByEmail(request.email)) throw CustomException(UserError.EMAIL_DUPLICATED)

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

        if (refreshToken != request.refreshToken) throw CustomException(JwtError.INVALID_TOKEN)

        return jwtProvider.generateToken(user.email)
    }
}