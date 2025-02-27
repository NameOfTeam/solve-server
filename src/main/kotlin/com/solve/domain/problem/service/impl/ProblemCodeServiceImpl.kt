package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.ProblemCode
import com.solve.domain.problem.dto.request.ProblemCodeCreateRequest
import com.solve.domain.problem.dto.response.ProblemCodeResponse
import com.solve.domain.problem.error.ProblemCodeError
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemCodeRepository
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.service.ProblemCodeService
import com.solve.global.common.enums.ProgrammingLanguage
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProblemCodeServiceImpl(
    private val problemCodeRepository: ProblemCodeRepository,
    private val problemRepository: ProblemRepository,
    private val securityHolder: SecurityHolder
) : ProblemCodeService {
    @Transactional
    override fun saveCode(problemId: Long, language: ProgrammingLanguage, request: ProblemCodeCreateRequest) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(
                ProblemError.PROBLEM_NOT_FOUND,
                problemId
            )
        val user = securityHolder.user
        val code = problemCodeRepository.findByProblemAndUserAndLanguage(problem, user, language)

        if (code != null) {
            code.code = request.code

            problemCodeRepository.save(code)
        } else {
            problemCodeRepository.save(ProblemCode(
                code = request.code,
                language = language,
                problem = problem,
                user = user
            ))
        }
    }

    @Transactional(readOnly = true)
    override fun getCode(problemId: Long, language: ProgrammingLanguage): ProblemCodeResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(
                ProblemError.PROBLEM_NOT_FOUND,
                problemId
            )
        val user = securityHolder.user
        val code =
            problemCodeRepository.findByProblemAndUserAndLanguage(problem, user, language)
                ?: throw CustomException(ProblemCodeError.PROBLEM_CODE_NOT_FOUND)

        return ProblemCodeResponse.of(code)
    }

    @Transactional
    override fun deleteCode(problemId: Long, language: ProgrammingLanguage) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(
                ProblemError.PROBLEM_NOT_FOUND,
                problemId
            )
        val user = securityHolder.user
        val code =
            problemCodeRepository.findByProblemAndUserAndLanguage(problem, user, language)
                ?: throw CustomException(ProblemCodeError.PROBLEM_CODE_NOT_FOUND)

        problemCodeRepository.delete(code)
    }
}