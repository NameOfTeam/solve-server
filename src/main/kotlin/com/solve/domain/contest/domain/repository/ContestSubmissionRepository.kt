package com.solve.domain.contest.domain.repository

import com.solve.domain.contest.domain.entity.ContestParticipant
import com.solve.domain.contest.domain.entity.ContestSubmission
import org.springframework.data.jpa.repository.JpaRepository

interface ContestSubmissionRepository: JpaRepository<ContestSubmission, Long> {
    fun findAllByParticipant(participant: ContestParticipant): List<ContestSubmission>
}