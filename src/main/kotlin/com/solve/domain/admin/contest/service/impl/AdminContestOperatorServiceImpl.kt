package com.solve.domain.admin.contest.service.impl

import com.solve.domain.admin.contest.dto.request.AdminContestOperatorAddRequest
import com.solve.domain.admin.contest.service.AdminContestOperatorService
import com.solve.domain.contest.domain.entity.ContestOperator
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.error.ContestOperatorError
import com.solve.domain.contest.domain.repository.ContestOperatorRepository
import com.solve.domain.contest.domain.repository.ContestRepository
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AdminContestOperatorServiceImpl(
    private val contestRepository: ContestRepository,
    private val userRepository: UserRepository,
    private val contestOperatorRepository: ContestOperatorRepository
) : AdminContestOperatorService {
    @Transactional
    override fun addContestOperator(contestId: Long, request: AdminContestOperatorAddRequest) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val user =
            userRepository.findByIdOrNull(request.userId) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_ID)

        if (contestOperatorRepository.existsByContestAndUser(contest, user))
            throw CustomException(ContestOperatorError.CONTEST_OPERATOR_ALREADY_EXISTS)

        contestOperatorRepository.save(ContestOperator(contest = contest, user = user))
    }

    @Transactional
    override fun removeContestOperator(contestId: Long, userId: UUID) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val user = userRepository.findByIdOrNull(userId) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_ID)
        val operator =
            contestOperatorRepository.findByContestAndUser(contest, user)
                ?: throw CustomException(ContestOperatorError.CONTEST_OPERATOR_NOT_FOUND)

        contestOperatorRepository.delete(operator)
    }
}