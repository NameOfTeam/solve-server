package com.solve.domain.contest.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateContestAnnouncementRequest(
    @field:NotBlank(message = "제목은 필수입니다.")
    @field:Size(max = 255, message = "제목은 255자를 초과할 수 없습니다.")
    val title: String?,
    @field:NotBlank(message = "내용은 필수입니다.")
    val content: String?,
    val important: Boolean?
)