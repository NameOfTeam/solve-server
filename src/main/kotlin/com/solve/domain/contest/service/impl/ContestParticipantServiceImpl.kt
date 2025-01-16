package com.solve.domain.contest.service.impl

import com.solve.domain.contest.domain.entity.ContestParticipant
import com.solve.domain.contest.domain.enums.ContestVisibility
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.error.ContestParticipantError
import com.solve.domain.contest.repository.ContestParticipantRepository
import com.solve.domain.contest.repository.ContestRepository
import com.solve.domain.contest.service.ContestParticipantService
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ContestParticipantServiceImpl(
    private val securityHolder: SecurityHolder,
    private val contestRepository: ContestRepository,
    private val contestParticipantRepository: ContestParticipantRepository
) : ContestParticipantService {
    @Transactional
    override fun joinContest(contestId: Long) {
        val user = securityHolder.user
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        if (contest.visibility == ContestVisibility.PRIVATE) throw CustomException(ContestParticipantError.CONTEST_PRIVATE)
        if (contestParticipantRepository.existsByContestAndUser(contest, user)) throw CustomException(
            ContestParticipantError.CONTEST_PARTICIPANT_ALREADY_EXISTS
        )
        if (contest.startAt.isBefore(LocalDateTime.now())) throw CustomException(ContestParticipantError.CONTEST_ALREADY_STARTED)
        if (contest.endAt.isBefore(LocalDateTime.now())) throw CustomException(ContestParticipantError.CONTEST_ALREADY_ENDED)

        contestParticipantRepository.save(ContestParticipant(user = user, contest = contest))
    }

    @Transactional
    override fun leaveContest(contestId: Long) {
        val user = securityHolder.user
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        if (contest.visibility == ContestVisibility.PRIVATE) throw CustomException(ContestParticipantError.CONTEST_PRIVATE)
        if (contest.startAt.isBefore(LocalDateTime.now())) throw CustomException(ContestParticipantError.CONTEST_ALREADY_STARTED)
        if (contest.endAt.isBefore(LocalDateTime.now())) throw CustomException(ContestParticipantError.CONTEST_ALREADY_ENDED)
        val participant = contestParticipantRepository.findByContestAndUser(contest, user)
            ?: throw CustomException(ContestParticipantError.CONTEST_PARTICIPANT_NOT_FOUND)

        contestParticipantRepository.delete(participant)
    }
}