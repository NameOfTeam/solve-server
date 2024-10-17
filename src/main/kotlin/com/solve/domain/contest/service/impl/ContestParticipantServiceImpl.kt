package com.solve.domain.contest.service.impl

import com.solve.domain.contest.domain.entity.ContestParticipant
import com.solve.domain.contest.dto.request.ContestParticipantAddRequest
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.error.ContestOperatorError
import com.solve.domain.contest.error.ContestParticipantError
import com.solve.domain.contest.repository.ContestRepository
import com.solve.domain.contest.service.ContestParticipantService
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ContestParticipantServiceImpl(
    private val securityHolder: SecurityHolder,
    private val contestRepository: ContestRepository,
    private val userRepository: UserRepository
) : ContestParticipantService {
    @Transactional
    override fun addContestParticipant(contestId: Long, request: ContestParticipantAddRequest) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val user =
            userRepository.findByIdOrNull(request.userId) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_ID)
        val operator = securityHolder.user

        if (contest.operators.none { it.user == operator }) {
            throw CustomException(ContestOperatorError.CONTEST_OPERATOR_NOT_FOUND)
        }

        if (contest.participants.any { it.user == user }) {
            throw CustomException(ContestParticipantError.CONTEST_PARTICIPANT_ALREADY_EXISTS)
        }

        contest.participants.add(ContestParticipant(contest = contest, user = user))

        contestRepository.save(contest)
    }

    @Transactional
    override fun removeContestParticipant(contestId: Long, userId: UUID) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val user = userRepository.findByIdOrNull(userId) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_ID)
        val operator = securityHolder.user

        if (contest.operators.none { it.user == operator }) {
            throw CustomException(ContestOperatorError.CONTEST_OPERATOR_NOT_FOUND)
        }

        if (contest.participants.none { it.user == user }) {
            throw CustomException(ContestParticipantError.CONTEST_PARTICIPANT_NOT_FOUND)
        }

        contest.participants.removeIf { it.user == user }

        contestRepository.save(contest)
    }
}