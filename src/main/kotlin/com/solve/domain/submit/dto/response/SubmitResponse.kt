package com.solve.domain.submit.dto.response

import com.solve.domain.submit.domain.entity.Submit
import com.solve.domain.submit.domain.enums.SubmitState

data class SubmitResponse(
    val id: Long,
    val result: SubmitState
) {
    companion object {
        fun of(submit: Submit) = SubmitResponse(
            id = submit.id!!,
            result = submit.state
        )
    }
}