package com.solve.domain.profile.presentation.controller

import com.solve.domain.profile.application.service.ProfileService
import com.solve.global.common.dto.BaseResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/profile")
class ProfileController(
    private val profileService: ProfileService
) {
    @GetMapping
    fun getProfile() = BaseResponse.of(profileService.getProfile())

    @GetMapping("/{username}")
    fun getProfileByUsername(@PathVariable username: String) = BaseResponse.of(profileService.getProfile(username))
}