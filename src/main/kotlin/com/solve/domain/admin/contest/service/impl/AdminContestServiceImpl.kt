package com.solve.domain.admin.contest.service.impl

import com.solve.domain.admin.contest.dto.request.AdminContestCreateRequest
import com.solve.domain.admin.contest.dto.request.AdminContestUpdateRequest
import com.solve.domain.admin.contest.dto.response.*
import com.solve.domain.admin.contest.service.AdminContestService
import com.solve.domain.contest.domain.entity.Contest
import com.solve.domain.contest.error.ContestError
import com.solve.domain.contest.domain.repository.ContestOperatorRepository
import com.solve.domain.contest.domain.repository.ContestParticipantRepository
import com.solve.domain.contest.domain.repository.ContestProblemRepository
import com.solve.domain.contest.domain.repository.ContestRepository
import com.solve.domain.problem.repository.ProblemRepository
import com.solve.domain.user.repository.UserRepository
import com.solve.global.error.CustomException
import com.solve.global.security.holder.SecurityHolder
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminContestServiceImpl(
    private val securityHolder: SecurityHolder,
    private val userRepository: UserRepository,
    private val problemRepository: ProblemRepository,
    private val contestRepository: ContestRepository,
    private val contestOperatorRepository: ContestOperatorRepository,
    private val contestParticipantRepository: ContestParticipantRepository,
    private val contestProblemRepository: ContestProblemRepository
) : AdminContestService {
    @Transactional(readOnly = true)
    override fun getContests(pageable: Pageable): Page<AdminContestResponse> {
        return contestRepository.findAll(pageable).map { it.toResponse() }
    }

    @Transactional(readOnly = true)
    override fun getContest(contestId: Long): AdminContestResponse {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        return contest.toResponse()
    }

    @Transactional
    override fun createContest(request: AdminContestCreateRequest) {
        // TODO createContest 메서드 구현
    }

    @Transactional
    override fun updateContest(contestId: Long, request: AdminContestUpdateRequest) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        request.title?.let { contest.title = it }
        request.description?.let { contest.description = it }
        request.startTime?.let { contest.startTime = it }
        request.endTime?.let { contest.endTime = it }
        request.isPublic?.let { contest.isPublic = it }
    }

    @Transactional
    override fun deleteContest(contestId: Long) {
        val contest =
            contestRepository.findByIdOrNull(contestId) ?: throw CustomException(ContestError.CONTEST_NOT_FOUND)

        contestRepository.delete(contest)
    }

    private fun Contest.toResponse() = AdminContestResponse(
        id = id!!,
        title = title,
        description = description,
        startTime = startTime,
        endTime = endTime,
        createdAt = createdAt,
        updatedAt = updatedAt,
        participants = contestParticipantRepository.findAllByContest(this)
            .map { AdminContestParticipantResponse.of(it) },
        operators = contestOperatorRepository.findAllByContest(this).map { AdminContestOperatorResponse.of(it) },
        problems = contestProblemRepository.findAllByContest(this).map { AdminContestProblemResponse.of(it) },
        isPublic = isPublic,
        isDeleted = isDeleted,
        isRegistrationOpen = isRegistrationOpen,
        owner = AdminContestOwnerResponse.of(owner)
    )
}