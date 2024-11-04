package com.solve.domain.contest.service.impl

import com.solve.domain.contest.domain.entity.ContestParticipant
import com.solve.domain.contest.domain.enums.ContestVisibility
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.error.ContestParticipantError
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
    private val contestRepository: ContestRepository
) : ContestParticipantService {
    @Transactional
    override fun joinContest(contestId: Long) {
        val user = securityHolder.user
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        if (contest.visibility == ContestVisibility.PRIVATE) throw CustomException(ContestParticipantError.CONTEST_PRIVATE)
        if (contest.participants.any { it.user == user }) throw CustomException(ContestParticipantError.CONTEST_PARTICIPANT_ALREADY_EXISTS)
        if (contest.startAt.isBefore(LocalDateTime.now())) throw CustomException(ContestParticipantError.CONTEST_ALREADY_STARTED)
        if (contest.endAt.isBefore(LocalDateTime.now())) throw CustomException(ContestParticipantError.CONTEST_ALREADY_ENDED)

        contest.participants.add(ContestParticipant(user = user, contest = contest))

        contestRepository.save(contest)
    }

    @Transactional
    override fun leaveContest(contestId: Long) {
        val user = securityHolder.user
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        if (contest.visibility == ContestVisibility.PRIVATE) throw CustomException(ContestParticipantError.CONTEST_PRIVATE)
        if (contest.startAt.isBefore(LocalDateTime.now())) throw CustomException(ContestParticipantError.CONTEST_ALREADY_STARTED)
        if (contest.endAt.isBefore(LocalDateTime.now())) throw CustomException(ContestParticipantError.CONTEST_ALREADY_ENDED)
        val participant = contest.participants.find { it.user == user }
            ?: throw CustomException(ContestParticipantError.CONTEST_PARTICIPANT_NOT_FOUND)

        contest.participants.remove(participant)

        contestRepository.save(contest)
    }
}