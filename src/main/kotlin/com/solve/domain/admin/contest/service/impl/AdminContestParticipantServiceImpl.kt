package com.solve.domain.admin.contest.service.impl

import com.solve.domain.admin.contest.dto.request.AdminContestParticipantAddRequest
import com.solve.domain.admin.contest.error.AdminContestError
import com.solve.domain.admin.contest.error.AdminContestParticipantError
import com.solve.domain.admin.contest.service.AdminContestParticipantService
import com.solve.domain.contest.domain.entity.ContestParticipant
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.domain.repository.ContestOperatorRepository
import com.solve.domain.contest.domain.repository.ContestParticipantRepository
import com.solve.domain.contest.domain.repository.ContestRepository
import com.solve.domain.user.error.UserError
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AdminContestParticipantServiceImpl(
    private val securityHolder: SecurityHolder,
    private val contestRepository: ContestRepository,
    private val userRepository: UserRepository,
    private val contestOperatorRepository: ContestOperatorRepository,
    private val contestParticipantRepository: ContestParticipantRepository
) : AdminContestParticipantService {
    @Transactional
    override fun addContestParticipant(contestId: Long, request: AdminContestParticipantAddRequest) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val user =
            userRepository.findByIdOrNull(request.userId) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_ID)
        val me = securityHolder.user

        if (!contestOperatorRepository.existsByContestAndUser(contest, me) && contest.owner != me)
            throw CustomException(AdminContestError.CONTEST_NOT_AUTHORIZED)

        if (contestParticipantRepository.existsByContestAndUser(contest, user))
            throw CustomException(AdminContestParticipantError.CONTEST_PARTICIPANT_ALREADY_EXISTS)

        contestParticipantRepository.save(ContestParticipant(contest = contest, user = user))
    }

    @Transactional
    override fun removeContestParticipant(contestId: Long, userId: UUID) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val user = userRepository.findByIdOrNull(userId) ?: throw CustomException(UserError.USER_NOT_FOUND_BY_ID)
        val me = securityHolder.user

        if (!contestOperatorRepository.existsByContestAndUser(contest, me) && contest.owner != me)
            throw CustomException(AdminContestError.CONTEST_NOT_AUTHORIZED)

        val participant = contestParticipantRepository.findByContestAndUser(contest, user)
            ?: throw CustomException(AdminContestParticipantError.CONTEST_PARTICIPANT_NOT_FOUND)

        contestParticipantRepository.delete(participant)
    }
}