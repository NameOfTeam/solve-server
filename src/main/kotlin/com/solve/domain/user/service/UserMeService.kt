package com.solve.domain.user.service

import com.solve.domain.user.dto.request.UserMePasswordUpdateRequest
import com.solve.domain.user.dto.request.UserMeUpdateRequest
import com.solve.domain.user.dto.response.UserMeResponse
import org.springframework.web.multipart.MultipartFile

interface UserMeService {
    fun getMe(): UserMeResponse
    fun updateMe(request: UserMeUpdateRequest): UserMeResponse
    fun updatePassword(request: UserMePasswordUpdateRequest): UserMeResponse
    fun updateAvatar(file: MultipartFile)
}