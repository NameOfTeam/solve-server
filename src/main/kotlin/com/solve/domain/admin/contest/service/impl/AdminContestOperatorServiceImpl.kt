package com.solve.domain.admin.contest.service.impl

import com.solve.domain.admin.contest.dto.request.AdminContestOperatorAddRequest
import com.solve.domain.admin.contest.service.AdminContestOperatorService
import com.solve.domain.contest.domain.entity.ContestOperator
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.error.ContestOperatorError
import com.solve.domain.contest.repository.ContestRepository
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
    private val userRepository: UserRepository
) : AdminContestOperatorService {
    @Transactional
    override fun addContestOperator(contestId: Long, request: AdminContestOperatorAddRequest) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val user =
            userRepository.findByIdOrNull(request.userId) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_ID)

        if (contest.operators.any { it.user == user }) {
            throw CustomException(ContestOperatorError.CONTEST_OPERATOR_ALREADY_EXISTS)
        }

        contest.operators.add(ContestOperator(contest = contest, user = user))

        contestRepository.save(contest)
    }

    @Transactional
    override fun removeContestOperator(contestId: Long, userId: UUID) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val user = userRepository.findByIdOrNull(userId) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_ID)

        if (contest.operators.none { it.user == user }) {
            throw CustomException(ContestOperatorError.CONTEST_OPERATOR_NOT_FOUND)
        }

        contest.operators.removeIf { it.user == user }

        contestRepository.save(contest)
    }
}