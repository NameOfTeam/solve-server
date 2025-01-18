package com.solve.domain.submit.dto.response

import com.solve.domain.submit.domain.entity.Submit
import com.solve.domain.submit.domain.enums.SubmitState
import com.solve.global.common.enums.ProgrammingLanguage

data class SubmitResponse(
    val id: Long,
    val result: SubmitState,
    val language: ProgrammingLanguage,
    val timeUsage: Long?,
    val memoryUsage: Long?,
) {
    companion object {
        fun of(submit: Submit) = SubmitResponse(
            id = submit.id!!,
            result = submit.state,
            language = submit.language,
            timeUsage = submit.timeUsage,
            memoryUsage = submit.memoryUsage
        )
    }
}