package com.solve.domain.problem.dto.response

import com.solve.domain.problem.domain.entity.ProblemSubmit
import com.solve.domain.problem.domain.enums.ProblemSubmitResult

data class ProblemSubmitResponse(
    val id: Long,
    val result: ProblemSubmitResult
) {
    companion object {
        fun of(submit: ProblemSubmit) = ProblemSubmitResponse(
            id = submit.id!!,
            result = submit.result
        )
    }
}