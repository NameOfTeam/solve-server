package com.solve.domain.problem.service.impl

import com.solve.domain.problem.domain.entity.ProblemCode
import com.solve.domain.problem.dto.request.ProblemCodeCreateRequest
import com.solve.domain.problem.dto.response.ProblemCodeResponse
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemCodeRepository
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.problem.service.ProblemCodeService
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
    override fun saveCode(problemId: Long, request: ProblemCodeCreateRequest) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND, problemId)

        val user = securityHolder.user

        val problemCode = problemCodeRepository.findByProblemAndUser(problem, user)

        if (problemCode != null) {
            problemCode.code = request.code
            problemCode.language = request.language

            problemCodeRepository.save(problemCode)
        } else {
            val code = ProblemCode(
                code = request.code,
                language = request.language,
                problem = problem,
                user = user
            )

            problemCodeRepository.save(code)
        }
    }

    @Transactional
    override fun getCode(problemId: Long): ProblemCodeResponse {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND, problemId)

        val user = securityHolder.user

        val code =
            problemCodeRepository.findByProblemAndUser(problem, user) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        return ProblemCodeResponse.of(code)
    }

    override fun deleteCode(problemId: Long) {
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND, problemId)

        val user = securityHolder.user

        val code =
            problemCodeRepository.findByProblemAndUser(problem, user) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        problemCodeRepository.delete(code)
    }
}