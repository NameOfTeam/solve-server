package com.solve.domain.contest.application.service

import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.domain.entity.ContestAnnouncement
import com.solve.domain.contest.domain.entity.ContestParticipant
import com.solve.domain.contest.domain.entity.ContestProblem
import com.solve.domain.contest.domain.enums.ContestSearchState
import com.solve.domain.contest.domain.enums.ContestState
import com.solve.domain.contest.domain.repository.*
import com.solve.domain.contest.presentation.dto.request.ContestProblemAddRequest
import com.solve.domain.contest.presentation.dto.request.CreateContestAnnouncementRequest
import com.solve.domain.contest.presentation.dto.request.UpdateContestAnnouncementRequest
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.error.ContestProblemError
import com.solve.domain.contest.presentation.dto.response.*
import com.solve.domain.problem.error.ProblemError
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.user.domain.entity.User
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ContestService(
    private val securityHolder: SecurityHolder,
    private val contestRepository: ContestRepository,
    private val problemRepository: ProblemRepository,
    private val contestQueryRepository: ContestQueryRepository,
    private val contestProblemRepository: ContestProblemRepository,
    private val contestOperatorRepository: ContestOperatorRepository,
    private val contestParticipantRepository: ContestParticipantRepository,
    private val contestAnnouncementRepository: ContestAnnouncementRepository
) {
    @Transactional(readOnly = true)
    fun getContests(pageable: Pageable): Page<ContestResponse> {
        return contestRepository.findAll(pageable).map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getContest(contestId: Long): ContestDetailResponse {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        return contest.toDetailResponse()
    }

    @Transactional(readOnly = true)
    fun searchContest(query: String, state: ContestSearchState?, pageable: Pageable): Page<ContestResponse> {
        val contests = contestQueryRepository.searchContest(query, state, pageable)

        return contests.map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    fun getContestAnnouncements(contestId: Long, pageable: Pageable): Page<ContestAnnouncementResponse> {
        val contest = contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val announcements = contestAnnouncementRepository.findAllByContest(contest, pageable)

        return announcements.map { ContestAnnouncementResponse.of(it) }
    }

    @Transactional(readOnly = true)
    fun getContestAnnouncement(contestId: Long, announcementId: Long): ContestAnnouncementResponse {
        val contest = contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val announcement = contestAnnouncementRepository.findByIdAndContest(announcementId, contest)
            ?: throw CustomException(ContestError.CONTEST_ANNOUNCEMENT_NOT_FOUND)

        return ContestAnnouncementResponse.of(announcement)
    }

    @Transactional
    fun createContestAnnouncement(contestId: Long, request: CreateContestAnnouncementRequest): ContestAnnouncementResponse {
        val contest = contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val me = securityHolder.user

        if (!contest.hasPermission(me))
            throw CustomException(ContestError.CONTEST_NOT_AUTHORIZED)

        val announcement = contestAnnouncementRepository.save(
            ContestAnnouncement(
                contest = contest,
                title = request.title,
                content = request.content,
                author = me,
                important = request.important
            )
        )

        return ContestAnnouncementResponse.of(announcement)
    }

    @Transactional
    fun updateContestAnnouncement(contestId: Long, announcementId: Long, request: UpdateContestAnnouncementRequest): ContestAnnouncementResponse {
        val contest = contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val announcement = contestAnnouncementRepository.findByIdAndContest(announcementId, contest)
            ?: throw CustomException(ContestError.CONTEST_ANNOUNCEMENT_NOT_FOUND)
        val me = securityHolder.user

        if (announcement.author != me)
            throw CustomException(ContestError.CONTEST_ANNOUNCEMENT_NOT_AUTHORIZED)

        request.title?.let { announcement.title = it }
        request.content?.let { announcement.content = it }
        request.important?.let { announcement.important = it }

        return ContestAnnouncementResponse.of(announcement)
    }

    @Transactional
    fun deleteContestAnnouncement(contestId: Long, announcementId: Long) {
        val contest = contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val announcement = contestAnnouncementRepository.findByIdAndContest(announcementId, contest)
            ?: throw CustomException(ContestError.CONTEST_ANNOUNCEMENT_NOT_FOUND)
        val me = securityHolder.user

        if (announcement.author != me)
            throw CustomException(ContestError.CONTEST_ANNOUNCEMENT_NOT_AUTHORIZED)

        contestAnnouncementRepository.delete(announcement)
    }

    @Transactional
    fun addContestToProblem(contestId: Long, request: ContestProblemAddRequest) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val problem =
            problemRepository.findByIdOrNull(request.problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)
        val me = securityHolder.user

        if (!contestOperatorRepository.existsByContestAndUser(contest, me) && contest.owner != me)
            throw CustomException(ContestError.CONTEST_NOT_AUTHORIZED)

        if (contestProblemRepository.existsByContestAndProblem(contest, problem))
            throw CustomException(ContestProblemError.CONTEST_PROBLEM_ALREADY_EXISTS)

        contestProblemRepository.save(ContestProblem(contest = contest, problem = problem, order = request.order, score = request.score))

        contestRepository.save(contest)
    }

    @Transactional
    fun removeContestFromProblem(contestId: Long, problemId: Long) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val problem =
            problemRepository.findByIdOrNull(problemId) ?: throw CustomException(ProblemError.PROBLEM_NOT_FOUND)

        if (!contestOperatorRepository.existsByContestAndUser(
                contest,
                securityHolder.user
            ) && contest.owner != securityHolder.user
        )
            throw CustomException(ContestError.CONTEST_NOT_AUTHORIZED)

        val contestProblem = contestProblemRepository.findByContestAndProblem(contest, problem)
            ?: throw CustomException(ContestProblemError.CONTEST_PROBLEM_NOT_FOUND)

        contestProblemRepository.delete(contestProblem)
    }

    @Transactional(readOnly = true)
    fun getContestParticipants(contestId: Long): List<ContestParticipantResponse> {
        val contest = contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        return contestParticipantRepository.findAllByContest(contest).map { ContestParticipantResponse.of(it) }
    }

    @Transactional
    fun removeContestParticipant(contestId: Long, participantId: Long) {
        val contest = contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val participant = contestParticipantRepository.findByIdOrNull(participantId) ?: throw CustomException(ContestError.CONTEST_PARTICIPANT_NOT_FOUND)
        val me = securityHolder.user
        val isOperator = contestOperatorRepository.existsByContestAndUser(contest, me)

        if (participant.contest != contest)
            throw CustomException(ContestError.CONTEST_PARTICIPANT_NOT_FOUND)

        if (contest.owner != me && !isOperator)
            throw CustomException(ContestError.CONTEST_NOT_AUTHORIZED)

        contestParticipantRepository.delete(participant)
    }

    @Transactional
    fun registerContest(contestId: Long) {
        val contest = contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)
        val me = securityHolder.user

        if (!contest.isRegistrationOpen)
            throw CustomException(ContestError.CONTEST_REGISTRATION_NOT_OPEN)

        val isAlreadyRegistered = contestParticipantRepository.existsByContestAndUser(contest, me)

        if (isAlreadyRegistered)
            throw CustomException(ContestError.CONTEST_ALREADY_REGISTERED)

        val participant = ContestParticipant(contest = contest, user = me)

        contestParticipantRepository.save(participant)
    }

    fun getContestLeaderboard(contestId: Long) {
        TODO("Not yet implemented")
    }

    fun exportContestLeaderboard(contestId: Long) {
        TODO("Not yet implemented")
    }

    fun Contest.hasPermission(user: User) = owner == user || contestOperatorRepository.existsByContestAndUser(this, user)
    fun Contest.toResponse() = ContestResponse(
        id = id!!,
        title = title,
        description = description,
        startTime = startTime,
        endTime = endTime,
        owner = ContestOwnerResponse.of(owner),
        state = when {
            startTime.isAfter(LocalDateTime.now()) -> ContestState.UPCOMING
            endTime.isBefore(LocalDateTime.now()) -> ContestState.ENDED
            else -> ContestState.ONGOING
        },
        winner = winner?.let { ContestWinnerResponse.of(it) },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    fun Contest.toDetailResponse() = ContestDetailResponse(
        id = id!!,
        title = title,
        description = description,
        startTime = startTime,
        endTime = endTime,
        owner = ContestOwnerResponse.of(owner),
        state = when {
            startTime.isAfter(LocalDateTime.now()) -> ContestState.UPCOMING
            endTime.isBefore(LocalDateTime.now()) -> ContestState.ENDED
            else -> ContestState.ONGOING
        },
        winner = winner?.let { ContestWinnerResponse.of(it) },
        operators = contestOperatorRepository.findAllByContest(this).map { ContestOperatorResponse.of(it) },
        participants = contestParticipantRepository.findAllByContest(this).map { ContestParticipantResponse.of(it) },
        problems = if (startTime.isBefore(LocalDateTime.now())) {
            contestProblemRepository.findAllByContest(this).map { ContestProblemResponse.of(it) }
        } else {
            emptyList()
        },
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}