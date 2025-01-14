package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.ProblemCode
import com.solve.domain.problem.dto.request.ProblemCodeCreateRequest
import com.solve.domain.problem.dto.request.ProblemCodeUpdateRequest
import com.solve.domain.problem.dto.response.ProblemCodeResponse
import com.solve.domain.problem.error.ProblemCodeError
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemCodeRepository
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.service.ProblemCodeService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProblemCodeServiceImpl(
    private val problemCodeRepository: ProblemCodeRepository,
    private val problemRepository: ProblemRepository,
    private val securityHolder: SecurityHolder
) : ProblemCodeService {
    override fun saveCode(problemId: Long, request: ProblemCodeCreateRequest): ProblemCodeResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND, problemId)

        val user = securityHolder.user

        if (problemCodeRepository.existsByProblemAndUser(problem, user)) {
            throw CustomException(ProblemCodeError.PROBLEM_CODE_ALREADY_EXIST)
        }

        val code = ProblemCode(
            code = request.code,
            language = request.language,
            problem = problem,
            user = user
        )

        problemCodeRepository.save(code)

        return ProblemCodeResponse.of(code)
    }

    override fun getCode(problemId: Long): ProblemCodeResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND, problemId)

        val user = securityHolder.user

        val code =
            problemCodeRepository.findByProblemAndUser(problem, user) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        return ProblemCodeResponse.of(code)
    }

    override fun updateCode(problemId: Long, request: ProblemCodeUpdateRequest): ProblemCodeResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND, problemId)

        val user = securityHolder.user

        val code =
            problemCodeRepository.findByProblemAndUser(problem, user) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        code.code = request.code ?: code.code
        code.language = request.language ?: code.language

        problemCodeRepository.save(code)

        return ProblemCodeResponse.of(code)
    }

    override fun deleteCode(problemId: Long): ProblemCodeResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND, problemId)

        val user = securityHolder.user

        val code =
            problemCodeRepository.findByProblemAndUser(problem, user) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        problemCodeRepository.delete(code)

        return ProblemCodeResponse.of(code)
    }
}