package com.solve.domain.problem.service

import com.solve.global.common.enums.ProgrammingLanguage
import org.springframework.web.socket.WebSocketSession

interface ProblemRunService {
    fun runCode(request: RunCodeRequest, session: WebSocketSession)
    fun stopCode(sessionId: String)
    fun handleInput(sessionId: String, input: String)
}

data class RunCodeRequest(
    val problemId: Long,
    val code: String,
    val language: ProgrammingLanguage
)